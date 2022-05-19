import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget
import org.jetbrains.kotlin.gradle.plugin.mpp.NativeBuildType
import org.jetbrains.kotlin.gradle.targets.native.tasks.KotlinNativeHostTest
import org.jetbrains.kotlin.konan.target.HostManager
import de.undercouch.gradle.tasks.download.Download

plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization")
    `maven-publish`
    signing
    id("de.undercouch.download")
}

val serialVersion: String by rootProject.extra
val jnaVersion: String by rootProject.extra
val olmVersion = "3.2.4"

val downloadsDir = buildDir.resolve("downloads")
val olmZip = downloadsDir.resolve("olm-$olmVersion.zip")
val olmDir = downloadsDir.resolve("olm-$olmVersion")

val downloadOlm by tasks.registering(Download::class) {
    src("https://gitlab.matrix.org/matrix-org/olm/-/archive/$olmVersion/olm-$olmVersion.zip")
    dest(olmZip)
    overwrite(false)
}
val extractOlm by tasks.registering(Copy::class) {
    from(downloadOlm.map { zipTree(it.dest) })
    into(downloadsDir)
}

val olmPath: String? = System.getenv("OLM_PATH")
if (olmPath == null) {
    println("{OLM_PATH} was not specified.")
}

kotlin {
    jvm()
    linuxX64()
    macosX64()
    mingwX64()
    iosArm32()
    iosArm64()
    iosX64()

    explicitApi()

    sourceSets {
        commonMain {
            dependencies {
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-core:$serialVersion")
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:$serialVersion")
            }
        }
        commonTest {
            dependencies {
                implementation(kotlin("test"))
            }
        }
        named("jvmMain") {
            dependencies {
                implementation("net.java.dev.jna:jna:$jnaVersion")
            }
        }
        val nativeMain by creating {
            dependsOn(commonMain.get())
        }
        val nativeTest by creating {
            dependsOn(commonTest.get())
        }

        for (target in targets.withType<KotlinNativeTarget>()) {
            val main = getByName("${target.name}Main")
            main.dependsOn(nativeMain)

            val test = getByName("${target.name}Test")
            test.dependsOn(nativeTest)
        }
    }

    targets.withType<KotlinNativeTarget> {
        compilations {
            "main" {
                cinterops {
                    create("libolm") {
                        tasks.named(interopProcessingTaskName) {
                            dependsOn(extractOlm)
                        }
                        includeDirs(olmDir.resolve("include"))
                    }
                }
            }
            "test" {
                binaries {
                    if (olmPath != null) {
                        getTest(NativeBuildType.DEBUG).linkerOpts("-L${olmPath}", "-lolm")
                    }
                }
            }
        }
    }

    // Hack until https://youtrack.jetbrains.com/issue/KT-30498
    targets.withType<KotlinNativeTarget> {
        // Disable cross-platform build
        if (konanTarget != HostManager.host) {
            binaries.all { linkTask.enabled = false }
        }
    }
}

tasks {
    // Setup search paths for libolm at runtime for tests
    named<Test>("jvmTest") {
        if (olmPath != null) {
            if (HostManager.hostIsMingw) {
                systemProperty("java.library.path", olmPath)
                systemProperty("jna.library.path", olmPath)
            } else {
                environment("LD_LIBRARY_PATH", olmPath)
            }
        }
    }
    withType<KotlinNativeHostTest> {
        if (olmPath != null) {
            if (HostManager.hostIsMingw) {
                environment("Path", olmPath)
            } else {
                environment("LD_LIBRARY_PATH", olmPath)
            }
        }
    }
}
