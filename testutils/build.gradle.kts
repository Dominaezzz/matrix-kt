import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget

plugins {
    kotlin("multiplatform")
}

val coroutineVersion: String by rootProject.extra

kotlin {
    jvm()
    js {
        browser {
        }
        nodejs {
        }
    }
    linuxX64()
    mingwX64()
    macosX64()
    iosArm32()
    iosArm64()
    iosX64()

    sourceSets {
        commonMain {
            dependencies {
                api("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutineVersion")
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
            }
        }
        named("jvmTest") {
            dependencies {
                implementation(kotlin("test"))
                implementation(kotlin("test-junit"))
            }
        }
        named("jsMain") {
            dependencies {
            }
        }
        named("jsTest") {
            dependencies {
                implementation(kotlin("test-js"))
            }
        }
        val nativeMain by creating {
            dependencies {
            }
        }
        val nativeTest by creating

        for (target in targets.withType<KotlinNativeTarget>()) {
            val main = getByName("${target.name}Main")
            main.dependsOn(nativeMain)

            val test = getByName("${target.name}Test")
            test.dependsOn(nativeTest)
        }
    }
}
