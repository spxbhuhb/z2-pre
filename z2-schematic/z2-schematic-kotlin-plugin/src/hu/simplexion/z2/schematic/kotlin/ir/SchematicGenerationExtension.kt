/*
 * Copyright © 2022-2023, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.z2.schematic.kotlin.ir

import hu.simplexion.z2.schematic.kotlin.ir.access.SchematicAccessTransform
import hu.simplexion.z2.schematic.kotlin.ir.klass.SchematicModuleTransform
import org.jetbrains.kotlin.backend.common.extensions.IrGenerationExtension
import org.jetbrains.kotlin.backend.common.extensions.IrPluginContext
import org.jetbrains.kotlin.ir.declarations.IrModuleFragment
import org.jetbrains.kotlin.ir.util.dump

internal class SchematicGenerationExtension: IrGenerationExtension {

    override fun generate(moduleFragment: IrModuleFragment, pluginContext: IrPluginContext) {

        SchematicPluginContext(pluginContext).apply {

            debug("schematic") { "".padEnd(80, '=') }
            debug("schematic") { moduleFragment.dump() }

            SchematicModuleTransform(this).also {
                // collects classes to transform and creates a SchematicClassTransform for each
                // does not call any functions in the SchematicClassTransform apart the constructor
                moduleFragment.accept(it, null)
                // calls SchematicClassTransform.transformFields for each class transform
                it.transformFields()
            }

            moduleFragment.accept(SchematicAccessTransform(this), null)
        }
    }
}

