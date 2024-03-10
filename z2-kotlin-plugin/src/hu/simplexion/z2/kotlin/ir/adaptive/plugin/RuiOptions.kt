/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.z2.kotlin.ir.adaptive.plugin

data class AdaptiveOptions(
    val annotations: List<String>,
    val dumpPoints: List<AdaptiveDumpPoint>,
    val rootNameStrategy: AdaptiveRootNameStrategy,
    val withTrace: Boolean,
    val exportState: Boolean,
    val importState: Boolean,
    val printDumps: Boolean,
    val pluginLogDir: String?
)