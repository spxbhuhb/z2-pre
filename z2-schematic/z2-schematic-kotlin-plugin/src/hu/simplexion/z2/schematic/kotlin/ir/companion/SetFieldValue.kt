/*
 * Copyright © 2022-2023, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.z2.schematic.kotlin.ir.companion

import hu.simplexion.z2.schematic.kotlin.ir.SCHEMATIC_COMPANION_SET_FIELD_VALUE
import hu.simplexion.z2.schematic.kotlin.ir.SchematicPluginContext
import hu.simplexion.z2.schematic.kotlin.ir.klass.SchematicFieldVisitor
import org.jetbrains.kotlin.backend.common.lower.DeclarationIrBuilder
import org.jetbrains.kotlin.ir.builders.*
import org.jetbrains.kotlin.ir.builders.declarations.addValueParameter
import org.jetbrains.kotlin.ir.declarations.IrSimpleFunction
import org.jetbrains.kotlin.ir.expressions.IrBranch
import org.jetbrains.kotlin.ir.types.IrType
import org.jetbrains.kotlin.ir.util.defaultType

class SetFieldValue(
    pluginContext: SchematicPluginContext,
    val companionTransform: CompanionTransform
) : AbstractCompanionFun(
    pluginContext,
    companionTransform,
    SCHEMATIC_COMPANION_SET_FIELD_VALUE,
    pluginContext.schematicCompanionSetFieldValue
) {

    companion object {
        const val ARG_INSTANCE = 0
        const val ARG_FIELD_NAME = 1
        const val ARG_VALUE = 2
    }

    override val returnType: IrType
        get() = irBuiltIns.nothingType

    override fun IrSimpleFunction.addParameters() {
        addValueParameter("instance", transformedClass.defaultType)
        addValueParameter("fieldName", irBuiltIns.stringType)
        addValueParameter("value", irBuiltIns.anyNType)
    }

    override fun IrSimpleFunction.buildBody() {
        body = DeclarationIrBuilder(irContext, this.symbol).irBlockBody {
            + irWhen(
                returnType,
                companionTransform.classTransform.fieldVisitors.map { toBranch(it, this@buildBody) }
            ).also {
                it.branches += irElseBranch(throwSchemaFieldNotFound(transformedClass, irGet(this@buildBody.valueParameters[ARG_FIELD_NAME])))
            }
        }
    }

    fun IrBlockBodyBuilder.toBranch(visitor: SchematicFieldVisitor, function: IrSimpleFunction): IrBranch {
        val property = visitor.property
        return irBranch(
            irEqual(irConst(property.name.identifier), irGet(function.valueParameters[ARG_FIELD_NAME])),

            if (property.setter != null) {
                irCall(
                    property.setter !!.symbol,
                    dispatchReceiver = irGet(function.valueParameters[ARG_INSTANCE]),
                ).also {
                    it.putValueArgument(0, irImplicitCast(irGet(function.valueParameters[ARG_VALUE]), property.getter !!.returnType))
                }
            } else {
                throwSchemaFieldNotFound(transformedClass, irGet(function.valueParameters[ARG_FIELD_NAME]))
            }
        )
    }

}
