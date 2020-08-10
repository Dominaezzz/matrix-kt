import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget
import org.jetbrains.kotlin.gradle.plugin.mpp.NativeBuildType
import org.jetbrains.kotlin.gradle.targets.native.tasks.KotlinNativeHostTest
import org.jetbrains.kotlin.konan.target.HostManager
import de.undercouch.gradle.tasks.download.Download

plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization")
    `maven-publish`
    id("de.undercouch.download")
}

val serialVersion: String by rootProject.extra
val jnaVersion: String by rootProject.extra
val olmVersion = "3.1.4"

val useSingleTarget: Boolean by extra(System.getProperty("idea.active") == "true")

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
if (HostManager.hostIsMingw) {
    if (olmPath == null) {
        println("{OLM_PATH} was not specified.")
    }
}

kotlin {
    jvm()
    if (useSingleTarget) {
        if (HostManager.hostIsLinux) linuxX64()
        if (HostManager.hostIsMac) macosX64()
        if (HostManager.hostIsMingw) mingwX64()
    } else {
        linuxX64()
        macosX64()
        mingwX64()
        iosArm32()
        iosArm64()
        iosX64()
    }

    sourceSets {
        commonMain {
            dependencies {
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-runtime-common:$serialVersion")
            }
        }
        commonTest {
            dependencies {
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
            }
        }
        named("jvmMain") {
            dependencies {
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-runtime:$serialVersion")
                implementation("net.java.dev.jna:jna:$jnaVersion")
            }
        }
        named("jvmTest") {
            dependencies {
                implementation(kotlin("test"))
                implementation(kotlin("test-junit"))
            }
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
                defaultSourceSet {
                    kotlin.srcDir("src/nativeMain/kotlin")
                    resources.srcDir("src/nativeMain/resources")
                }
                dependencies {
                    implementation("org.jetbrains.kotlinx:kotlinx-serialization-runtime-native:$serialVersion")
                }
            }
            "test" {
                binaries {
                    if (HostManager.hostIsLinux || HostManager.hostIsMac) {
                        getTest(NativeBuildType.DEBUG).linkerOpts("-L/usr/lib", "-L/usr/local/lib", "-lolm")
                    }
                    if (HostManager.hostIsMingw && olmPath != null) {
                        getTest(NativeBuildType.DEBUG).linkerOpts("-L${olmPath}", "-lolm")
                    }
                }
                defaultSourceSet {
                    kotlin.srcDir("src/nativeTest/kotlin")
                    resources.srcDir("src/nativeTest/resources")
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
        environment("LD_LIBRARY_PATH", "/usr/local/lib")
        if (HostManager.hostIsMingw && olmPath != null) {
            systemProperty("java.library.path", olmPath)
            systemProperty("jna.library.path", olmPath)
        }
    }
    withType<KotlinNativeHostTest> {
        environment("LD_LIBRARY_PATH", "/usr/local/lib")
        if (HostManager.hostIsMingw && olmPath != null) {
            environment("Path", olmPath)
        }
    }
}
