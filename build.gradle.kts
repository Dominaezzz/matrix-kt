plugins {
    kotlin("multiplatform") version "1.3.72" apply false
    kotlin("plugin.serialization") version "1.3.72" apply false
    id("de.undercouch.download") version "4.0.4" apply false
}

val ktorVersion: String by extra("1.3.2")
val serialVersion: String by extra("0.20.0")
val coroutineVersion: String by extra("1.3.5")
val jnaVersion: String by extra("5.5.0")

subprojects {
    repositories {
        jcenter()
        mavenCentral()
    }

    group = "io.github.matrixkt"
    version = "0.0.1"
}
