plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization")
    `maven-publish`
}

val ktorVersion: String by rootProject.extra
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

    sourceSets {
        commonMain {
            kotlin.srcDir("src/main/kotlin")
            resources.srcDir("src/main/resources")

            dependencies {
                api("org.jetbrains.kotlinx:kotlinx-serialization-runtime:$serialVersion")
                api("io.ktor:ktor-client-core:$ktorVersion")

                implementation("io.ktor:ktor-client-json:$ktorVersion")
                implementation("io.ktor:ktor-client-serialization:$ktorVersion")
            }
        }
        commonTest {
            kotlin.srcDir("src/test/kotlin")
            resources.srcDir("src/test/resources")

            dependencies {
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))

                implementation("io.ktor:ktor-client-mock:$ktorVersion")
                implementation(project(":testutils"))
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
    }
}
