/*
 * Copyright © 2022-2023, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
rootProject.name = "z2-exposed-runtime"

pluginManagement {
    includeBuild("../../z2-schematic/z2-schematic-gradle-plugin")
    repositories {
        mavenLocal()
        mavenCentral()
        gradlePluginPortal()
    }
}

includeBuild("../../z2-commons/z2-commons-runtime")
includeBuild("../../z2-service/z2-service-runtime")
includeBuild("../../z2-schematic/z2-schematic-runtime")