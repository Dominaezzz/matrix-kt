plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization")
    `maven-publish`
}

val ktorVersion: String by rootProject.extra
val serialVersion: String by rootProject.extra
val coroutineVersion: String by rootProject.extra

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
                api("io.ktor:ktor-client-core:$ktorVersion")

                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutineVersion")
                implementation("io.ktor:ktor-client-json:$ktorVersion")
                implementation("io.ktor:ktor-client-serialization:$ktorVersion")
            }
        }
        commonTest {
            kotlin.srcDir("src/test/kotlin")
            resources.srcDir("src/test/resources")

            dependencies {
                implementation(kotlin("test"))

                implementation("io.ktor:ktor-client-mock:$ktorVersion")
                implementation(project(":testutils"))
            }
        }
    }
}
