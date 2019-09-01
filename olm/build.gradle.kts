import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget
import org.jetbrains.kotlin.gradle.plugin.mpp.NativeBuildType
import org.jetbrains.kotlin.gradle.targets.native.tasks.KotlinNativeHostTest
import org.jetbrains.kotlin.konan.target.HostManager

plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization")
    `maven-publish`
}

val serialVersion: String by rootProject.extra
val jnaVersion: String by rootProject.extra

val useSingleTarget: Boolean by extra(System.getProperty("idea.active") == "true")

kotlin {
    jvm()
    if (useSingleTarget) {
        if (HostManager.hostIsLinux) linuxX64()
        if (HostManager.hostIsMac) macosX64()
    } else {
        linuxX64()
        macosX64()
        iosArm32()
        iosArm64()
        iosX64()
    }

    sourceSets {
        commonMain {
            dependencies {
                implementation(kotlin("stdlib-common"))
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
                implementation(kotlin("stdlib-jdk8"))
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
                        if (HostManager.hostIsLinux || HostManager.hostIsMac) {
                            includeDirs("/usr/include", "/usr/local/include")
                        }
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
    }
    withType<KotlinNativeHostTest> {
        environment("LD_LIBRARY_PATH", "/usr/local/lib")
    }
}
