/*
 * Copyright © 2023, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
plugins {
    kotlin("multiplatform") version "1.9.0"
    java
    signing
    `maven-publish`
}

group = "hu.simplexion.z2"
val baseName = "z2-boot"
val pomName = "Z2 Boot"
val scmPath = "spxbhuhb/z2"

repositories {
    mavenLocal()
    mavenCentral()
    google()
}

val z2_browser_version: String by project
val z2_commons_version: String by project
val z2_module_base_version: String by project
val z2_exposed_version: String by project
val z2_service_version: String by project
val z2_schematic_version: String by project

kotlin {
    jvm {
        jvmToolchain(11)
    }
    js(IR) {
        browser()
        binaries.library()
    }
    sourceSets {
        commonMain {
            dependencies {
                api("hu.simplexion.z2:z2-commons:${z2_commons_version}")
                api("hu.simplexion.z2:z2-module-base:${z2_module_base_version}")
                api("hu.simplexion.z2:z2-service-runtime:${z2_service_version}")
                api("hu.simplexion.z2:z2-schematic-runtime:${z2_schematic_version}")
                api("hu.simplexion.z2:z2-service-ktor:${z2_service_version}")
            }
        }
        commonTest {
            dependencies {
                api(kotlin("test"))
            }
        }
        sourceSets["jsMain"].dependencies {
            api("hu.simplexion.z2:z2-browser-runtime:${z2_browser_version}")
        }
        sourceSets["jvmMain"].dependencies {
            api("hu.simplexion.z2:z2-exposed-runtime:${z2_exposed_version}")
        }
    }
}

tasks["clean"].dependsOn(gradle.includedBuilds.map { it.task(":clean") })
tasks["build"].dependsOn(gradle.includedBuilds.map { it.task(":build") })
tasks["publishToMavenLocal"].dependsOn(gradle.includedBuilds.map { it.task(":publishToMavenLocal") })
tasks["publish"].dependsOn(gradle.includedBuilds.map { it.task(":publish") })

// ----------------------------------------------------------------
// DO NOT EDIT BELOW THIS, ASK FIRST!
// ----------------------------------------------------------------

val publishSnapshotUrl = (System.getenv("Z2_PUBLISH_SNAPSHOT_URL") ?: project.findProperty("z2.publish.snapshot.url"))?.toString()
val publishReleaseUrl = (System.getenv("Z2_PUBLISH_RELEASE_URL") ?: project.findProperty("z2.publish.release.url"))?.toString()
val publishUsername = (System.getenv("Z2_PUBLISH_USERNAME") ?: project.findProperty("z2.publish.username"))?.toString()
val publishPassword = (System.getenv("Z2_PUBLISH_PASSWORD") ?: project.findProperty("z2.publish.password"))?.toString()
val isSnapshot = "SNAPSHOT" in project.version.toString()
val isPublishing = project.properties["z2.publish"] != null || System.getenv("Z2_PUBLISH") != null

fun RepositoryHandler.mavenRepo(repoUrl: String) {
    maven {
        url = project.uri(repoUrl)
        name = "Central"
        isAllowInsecureProtocol = true
        credentials {
            username = publishUsername
            password = publishPassword
        }
    }
}

publishing.publications.withType<MavenPublication> {
    val publication = this
    val javadocJar = tasks.register("${publication.name}JavadocJar", Jar::class) {
        archiveClassifier.set("javadoc")
        // Each archive name should be distinct. Mirror the format for the sources Jar tasks.
        archiveBaseName.set("${archiveBaseName.get()}-${publication.name}")
    }
    artifact(javadocJar)
}

if (isPublishing) {
    tasks.withType<Jar> {
        manifest {
            attributes += sortedMapOf(
                "Built-By" to System.getProperty("user.name"),
                "Build-Jdk" to System.getProperty("java.version"),
                "Implementation-Vendor" to "Simplexion Kft.",
                "Implementation-Version" to archiveVersion.get(),
                "Created-By" to GradleVersion.current()
            )
        }
    }

    signing {
        if (project.properties["signing.keyId"] == null) {
            useGpgCmd()
        }
        sign(publishing.publications)
    }

    publishing {

        repositories {
            if (isSnapshot) {
                requireNotNull(publishSnapshotUrl) { throw IllegalStateException("publishing: missing snapshot url, define Z2_PUBLISH_SNAPSHOT_URL") }
                mavenRepo(publishSnapshotUrl)
            } else {
                requireNotNull(publishReleaseUrl) { throw IllegalStateException("publishing: missing release url, define Z2_PUBLISH_RELEASE_URL") }
                mavenRepo(publishReleaseUrl)
            }
        }

        publications.withType<MavenPublication>().all {
            pom {
                description.set(project.name)
                name.set(pomName)
                url.set("https://github.com/$scmPath")
                scm {
                    url.set("https://github.com/$scmPath")
                    connection.set("scm:git:git://github.com/$scmPath.git")
                    developerConnection.set("scm:git:ssh://git@github.com/$scmPath.git")
                }
                licenses {
                    license {
                        name.set("Apache 2.0")
                        url.set("https://www.apache.org/licenses/LICENSE-2.0.txt")
                        distribution.set("repo")
                    }
                }
                developers {
                    developer {
                        id.set("toth-istvan-zoltan")
                        name.set("Tóth István Zoltán")
                        url.set("https://github.com/toth-istvan-zoltan")
                        organization.set("Simplexion Kft.")
                        organizationUrl.set("https://www.simplexion.hu")
                    }
                }
            }
        }
    }
}