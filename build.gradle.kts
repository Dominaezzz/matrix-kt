plugins {
    kotlin("multiplatform") version "1.3.61" apply false
    kotlin("plugin.serialization") version "1.3.61" apply false
}

subprojects {
    repositories {
        jcenter()
        mavenCentral()
    }

    group = "io.github.matrixkt"
    version = "0.0.1"
}
