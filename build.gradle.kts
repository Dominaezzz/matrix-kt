import org.jetbrains.kotlin.gradle.tasks.KotlinTest

plugins {
    kotlin("multiplatform") version "1.3.41" apply false
    id("kotlinx-serialization") version "1.3.41" apply false
}

subprojects {
    repositories {
        jcenter()
        mavenCentral()
    }

    group = "io.github.matrixkt"
    version = "0.0.1"

    //TODO: Remove after Kotlin 1.3.50 fixes https://youtrack.jetbrains.net/issue/KT-33246
    afterEvaluate {
        tasks.withType<KotlinTest> {
            binaryResultsDirectory.set(binResultsDir)
        }
    }
}
