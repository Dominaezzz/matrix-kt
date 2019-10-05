import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget

plugins {
    kotlin("multiplatform")
}

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

    val coroutineVersion = "1.3.2"

    sourceSets {
        commonMain {
            dependencies {
                implementation(kotlin("stdlib-common"))

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
                implementation(kotlin("stdlib-jdk8"))
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
                implementation(kotlin("stdlib-js"))

                api("org.jetbrains.kotlinx:kotlinx-coroutines-core-js:$coroutineVersion")
            }
        }
        named("jsTest") {
            dependencies {
                implementation(kotlin("test-js"))
            }
        }
    }

    targets.withType<KotlinNativeTarget> {
        compilations.named("main") {
            defaultSourceSet {
                kotlin.srcDir("src/nativeMain/kotlin")
            }

            dependencies {
                api("org.jetbrains.kotlinx:kotlinx-coroutines-core-native:$coroutineVersion")
            }
        }
        compilations.named("test") {
            defaultSourceSet {
                kotlin.srcDir("src/nativeTest/kotlin")
            }
        }
    }
}
