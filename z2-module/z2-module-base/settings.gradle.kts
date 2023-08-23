/*
 * Copyright © 2023, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
rootProject.name = "z2-module-base"

pluginManagement {
    includeBuild("../../z2-schematic/z2-schematic-gradle-plugin")
    includeBuild("../../z2-service/z2-service-gradle-plugin")
}

includeBuild("../../z2-commons")
includeBuild("../../z2-service/z2-service-runtime")
includeBuild("../../z2-schematic/z2-schematic-runtime")
includeBuild("../../z2-exposed/z2-exposed-runtime")
includeBuild("../../z2-browser/z2-browser-runtime")