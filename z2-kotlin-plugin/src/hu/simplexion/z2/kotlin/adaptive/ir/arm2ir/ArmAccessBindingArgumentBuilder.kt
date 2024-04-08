package hu.simplexion.z2.kotlin.adaptive.ir.arm2ir

import hu.simplexion.z2.kotlin.adaptive.Indices
import hu.simplexion.z2.kotlin.adaptive.ir.arm.ArmAccessBindingArgument
import hu.simplexion.z2.kotlin.adaptive.ir.arm.ArmClosure
import org.jetbrains.kotlin.ir.declarations.IrSimpleFunction
import org.jetbrains.kotlin.ir.declarations.IrValueParameter
import org.jetbrains.kotlin.ir.declarations.IrVariable
import org.jetbrains.kotlin.ir.expressions.IrExpression
import org.jetbrains.kotlin.ir.expressions.impl.IrConstructorCallImpl
import org.jetbrains.kotlin.ir.types.classFqName
import org.jetbrains.kotlin.ir.types.defaultType
import org.jetbrains.kotlin.ir.util.SYNTHETIC_OFFSET
import org.jetbrains.kotlin.ir.util.constructors

class ArmAccessBindingArgumentBuilder(
    parent: ClassBoundIrBuilder,
    val argument: ArmAccessBindingArgument,
    closure: ArmClosure,
    fragment: IrValueParameter,
    closureDirtyMask: IrVariable
) : ArmValueArgumentBuilder(parent, argument, closure, fragment, closureDirtyMask) {

    override fun patchBody(patchFun: IrSimpleFunction): IrExpression =
        irSetDescendantStateVariable(
            patchFun,
            argument.argumentIndex,
            IrConstructorCallImpl(
                SYNTHETIC_OFFSET, SYNTHETIC_OFFSET,
                pluginContext.adaptiveAccessBindingClass.defaultType,
                pluginContext.adaptiveAccessBindingClass.constructors.first(),
                0, 0,
                Indices.ADAPTIVE_ACCESS_BINDING_ARGUMENT_COUNT,
            ).apply {
                putValueArgument(Indices.ADAPTIVE_ACCESS_BINDING_OWNER, irGet(patchFun.dispatchReceiverParameter !!))
                putValueArgument(Indices.ADAPTIVE_ACCESS_BINDING_INDEX_IN_STATE, irConst(argument.indexInState))
                putValueArgument(Indices.ADAPTIVE_ACCESS_BINDING_INDEX_IN_CLOSURE, irConst(argument.indexInClosure))
                putValueArgument(
                    Indices.ADAPTIVE_ACCESS_BINDING_METADATA,
                    IrConstructorCallImpl(
                        SYNTHETIC_OFFSET, SYNTHETIC_OFFSET,
                        pluginContext.adaptiveAccessBindingClass.defaultType,
                        pluginContext.adaptiveAccessBindingClass.constructors.first(),
                        0, 0,
                        Indices.ADAPTIVE_PROPERTY_METADATA_ARGUMENT_COUNT,
                    ).apply {
                        putValueArgument(Indices.ADAPTIVE_PROPERTY_METADATA_TYPE, irConst(argument.value.type.classFqName!!.asString()))
                    }
                )

            }
        )

}