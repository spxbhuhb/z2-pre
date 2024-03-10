/*
 * Copyright 2010-2020 JetBrains s.r.o. and Kotlin Programming Language contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the license/LICENSE.txt file.
 */
package hu.simplexion.z2.kotlin.ir.adaptive.plugin

import hu.simplexion.z2.kotlin.ir.adaptive.*
import org.jetbrains.kotlin.config.CompilerConfigurationKey

object AdaptiveConfigurationKeys {
    val ANNOTATION: CompilerConfigurationKey<List<String>> = CompilerConfigurationKey.create(OPTION_NAME_ANNOTATION)
    val DUMP: CompilerConfigurationKey<List<AdaptiveDumpPoint>> = CompilerConfigurationKey.create(OPTION_NAME_DUMP_POINT)
    val ROOT_NAME_STRATEGY: CompilerConfigurationKey<AdaptiveRootNameStrategy> = CompilerConfigurationKey.create(OPTION_NAME_ROOT_NAME_STRATEGY)
    val TRACE: CompilerConfigurationKey<Boolean> = CompilerConfigurationKey.create(OPTION_NAME_TRACE)
    val EXPORT_STATE: CompilerConfigurationKey<Boolean> = CompilerConfigurationKey.create(OPTION_NAME_EXPORT_STATE)
    val IMPORT_STATE: CompilerConfigurationKey<Boolean> = CompilerConfigurationKey.create(OPTION_NAME_IMPORT_STATE)
    val PRINT_DUMPS: CompilerConfigurationKey<Boolean> = CompilerConfigurationKey.create(OPTION_NAME_PRINT_DUMPS)
    val PLUGIN_LOG_DIR: CompilerConfigurationKey<String> = CompilerConfigurationKey.create(OPTION_NAME_PLUGIN_LOG_DIR)
}

