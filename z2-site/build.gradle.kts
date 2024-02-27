/*
 * Copyright © 2023, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
plugins {
    kotlin("multiplatform") version "1.9.10"
    id("hu.simplexion.z2") version "2024.02.27"

    java
    application
    id("com.github.johnrengelman.shadow") version "7.1.2"
}

group = "hu.simplexion.z2"
val baseName = "z2-site"
val pomName = "Z2 Site"
val scmPath = "spxbhuhb/z2"

val ktor_version: String by project
val logback_version: String by project

repositories {
    mavenLocal()
    mavenCentral()
    google()
}

application {
    mainClass.set("MainKt")
}

kotlin {
    sourceSets.all {
        languageSettings {
            languageVersion = "2.0"
        }
    }

    jvmToolchain(11)

    jvm {
        withJava()
    }

    js(IR) {
        browser()
        binaries.executable()

        val targetDirectory = file("$buildDir/processedResources/js/main/")

        tasks.register("z2CssExtract") {
            doLast {
                targetDirectory.mkdirs()
                compilations["main"].runtimeDependencyFiles.firstOrNull {
                    "z2-lib" in it.name
                }?.let {
                    copy {
                        include("**/*.css")
                        from(zipTree(it))
                        into(targetDirectory)
                        duplicatesStrategy = DuplicatesStrategy.WARN
                    }
                }
            }
        }

        tasks["jsBrowserDistribution"].dependsOn("z2CssExtract")
    }

    sourceSets["commonMain"].dependencies {
        implementation("hu.simplexion.z2:z2-core:$version")
        implementation("hu.simplexion.z2:z2-lib:$version")
    }
    sourceSets["jsMain"].dependencies {
        implementation("io.ktor:ktor-client-websockets:$ktor_version")
    }
    sourceSets["jvmMain"].dependencies {
        implementation("io.ktor:ktor-server-core-jvm:$ktor_version")
        implementation("io.ktor:ktor-server-netty-jvm:$ktor_version")
        implementation("io.ktor:ktor-server-websockets:$ktor_version")

        implementation("ch.qos.logback:logback-classic:$logback_version")

        implementation("org.postgresql:postgresql:42.2.2")
        implementation("com.zaxxer:HikariCP:3.4.2")
    }
    sourceSets["jvmTest"].dependencies {
        implementation("com.h2database:h2:2.1.214")
    }
}

tasks.withType<Jar> {
    duplicatesStrategy = DuplicatesStrategy.WARN
}