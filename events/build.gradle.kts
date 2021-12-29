plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization")
    `maven-publish`
    signing
}

val serialVersion: String by rootProject.extra

kotlin {
    jvm()
    js {
        browser()
        nodejs()
    }
    linuxX64()
    mingwX64()
    macosX64()
    iosArm32()
    iosArm64()
    iosX64()

    explicitApi()

    sourceSets {
        commonMain {
            kotlin.srcDir("src/main/kotlin")
            resources.srcDir("src/main/resources")

            dependencies {
                implementation(kotlin("stdlib"))
                api("org.jetbrains.kotlinx:kotlinx-serialization-core:$serialVersion")
                api("org.jetbrains.kotlinx:kotlinx-serialization-json:$serialVersion")
            }
        }
        commonTest {
            kotlin.srcDir("src/test/kotlin")
            resources.srcDir("src/test/resources")

            dependencies {
                implementation(kotlin("test"))
            }
        }
    }
}
