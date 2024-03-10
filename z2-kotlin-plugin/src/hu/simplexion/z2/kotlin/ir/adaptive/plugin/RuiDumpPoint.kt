/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.z2.kotlin.ir.adaptive.plugin

import hu.simplexion.z2.kotlin.ir.adaptive.AdaptivePluginContext

enum class AdaptiveDumpPoint(
    val optionValue: String
) {
    Before("before"),
    After("after"),
    AdaptiveTree("adaptive-tree"),
    AirTree("air-tree"),
    KotlinLike("kotlin-like");

    fun dump(adaptivePluginContext: AdaptivePluginContext, dumpFunc: () -> Unit) {
        if (this in adaptivePluginContext.dumpPoints) dumpFunc()
    }

    companion object {
        fun optionValues(): List<String> = entries.map { it.optionValue }
        fun fromOption(value: String): AdaptiveDumpPoint? = entries.firstOrNull { it.optionValue == value }
    }
}