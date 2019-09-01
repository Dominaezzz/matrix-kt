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
}
