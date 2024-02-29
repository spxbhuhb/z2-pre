/*
 * Copyright © 2022-2023, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.z2.kotlin.schematic.ir

import hu.simplexion.z2.kotlin.Z2Options
import hu.simplexion.z2.kotlin.schematic.ir.access.SchematicAccessTransform
import org.jetbrains.kotlin.backend.common.extensions.IrGenerationExtension
import org.jetbrains.kotlin.backend.common.extensions.IrPluginContext
import org.jetbrains.kotlin.ir.declarations.IrModuleFragment

class SchematicGenerationExtension(
    val options: Z2Options
) : IrGenerationExtension {

    override fun generate(moduleFragment: IrModuleFragment, pluginContext: IrPluginContext) {
        SchematicPluginContext(pluginContext).apply {
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