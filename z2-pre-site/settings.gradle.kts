/*
 * Copyright © 2023, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
rootProject.name = "z2-pre-site"

pluginManagement {
    includeBuild("../z2-pre-gradle-plugin")
}

includeBuild("../z2-pre-lib")
includeBuild("../z2-pre-core")