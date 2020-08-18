import org.jetbrains.kotlin.konan.target.HostManager
import java.io.ByteArrayOutputStream

plugins {
    kotlin("multiplatform") version "1.4.0" apply false
    kotlin("plugin.serialization") version "1.4.0" apply false
    id("de.undercouch.download") version "4.0.4" apply false
}

val ktorVersion: String by extra("1.3.2-1.4.0-rc")
val serialVersion: String by extra("1.0.0-RC")
val coroutineVersion: String by extra("1.3.9")
val jnaVersion: String by extra("5.5.0")

val stdout = ByteArrayOutputStream()
exec {
    commandLine("git", "describe", "--tags")
    standardOutput = stdout
}

version = stdout.toString().trim()

subprojects {
    repositories {
        jcenter()
        mavenCentral()
    }

    group = "io.github.matrixkt"
    version = rootProject.version

    plugins.withId("maven-publish") {
        configure<PublishingExtension> {
            val vcs: String by project
            val bintrayOrg: String by project
            val bintrayRepository: String by project

            repositories {
                maven("https://api.bintray.com/maven/$bintrayOrg/$bintrayRepository/matrix-kt/;publish=0;override=0") {
                    name = "bintray"
                    credentials {
                        username = System.getenv("BINTRAY_USER")
                        password = System.getenv("BINTRAY_API_KEY")
                    }
                }
            }

            publications.withType<MavenPublication> {
                pom {
                    name.set(project.name)
                    description.set(project.description)
                    url.set(vcs)
                    licenses {
                        license {
                            name.set("The Apache Software License, Version 2.0")
                            url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
                            distribution.set("repo")
                        }
                    }
                    developers {
                        developer {
                            id.set("Dominaezzz")
                            name.set("Dominic Fischer")
                        }
                    }
                    scm {
                        connection.set("$vcs.git")
                        developerConnection.set("$vcs.git")
                        url.set(vcs)
                    }
                }
            }
        }

        val publishTasks = tasks.withType<PublishToMavenRepository>()
            .matching {
                when {
                    HostManager.hostIsMingw -> it.name.startsWith("publishMingw")
                    HostManager.hostIsMac -> it.name.startsWith("publishMacos") || it.name.startsWith("publishIos")
                    HostManager.hostIsLinux -> it.name.startsWith("publishLinux") ||
                            it.name.startsWith("publishJs") ||
                            it.name.startsWith("publishJvm") ||
                            it.name.startsWith("publishMetadata") ||
                            it.name.startsWith("publishKotlinMultiplatform")
                    else -> TODO("Unknown host")
                }
            }
        tasks.register("smartPublish") {
            dependsOn(publishTasks)
        }
    }
}
