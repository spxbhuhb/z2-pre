/*
 * Copyright © 2022-2023, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.z2.schematic.kotlin.ir.companion

import hu.simplexion.z2.schematic.kotlin.ir.SCHEMATIC_COMPANION_NEW_INSTANCE
import hu.simplexion.z2.schematic.kotlin.ir.SchematicPluginContext
import org.jetbrains.kotlin.backend.common.lower.DeclarationIrBuilder
import org.jetbrains.kotlin.ir.builders.irBlockBody
import org.jetbrains.kotlin.ir.builders.irReturn
import org.jetbrains.kotlin.ir.declarations.IrSimpleFunction
import org.jetbrains.kotlin.ir.expressions.impl.IrConstructorCallImpl
import org.jetbrains.kotlin.ir.util.SYNTHETIC_OFFSET
import org.jetbrains.kotlin.ir.util.defaultType
import org.jetbrains.kotlin.ir.util.primaryConstructor

/**
 * Transform the `newInstance` function of the companion. The function
 * may be:
 *
 * - missing, when the companion class is created by the plugin - add
 * - fake override, when the companion is declared but the property is not overridden - convert
 * - override, when there is an actual implementation - do not touch
 */
class NewInstance(
    pluginContext: SchematicPluginContext,
    companionTransform: CompanionTransform,
) : AbstractCompanionFun(
    pluginContext,
    companionTransform,
    SCHEMATIC_COMPANION_NEW_INSTANCE,
    pluginContext.schematicCompanionNewInstance
) {

    override fun IrSimpleFunction.buildBody() {
        body = DeclarationIrBuilder(irContext, this.symbol).irBlockBody {
            + irReturn(
                IrConstructorCallImpl(
                    SYNTHETIC_OFFSET, SYNTHETIC_OFFSET,
                    transformedClass.defaultType,
                    transformedClass.primaryConstructor !!.symbol,
                    0, 0, 0
                )
            )
        }
    }

}
