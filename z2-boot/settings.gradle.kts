/*
 * Copyright © 2023, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
rootProject.name = "z2-boot"

pluginManagement {
    includeBuild("../z2-gradle-plugin")
    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()
    }
}

includeBuild("../z2-commons")
includeBuild("../z2-browser")