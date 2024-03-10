package hu.simplexion.z2.kotlin.adaptive.ir.rum2air

import hu.simplexion.z2.kotlin.adaptive.Names
import hu.simplexion.z2.kotlin.adaptive.Strings
import hu.simplexion.z2.kotlin.adaptive.Strings.toNameWithPostfix
import hu.simplexion.z2.kotlin.adaptive.ir.ClassBoundIrBuilder
import hu.simplexion.z2.kotlin.adaptive.ir.air.AirDirtyMask
import hu.simplexion.z2.kotlin.adaptive.ir.rum.RumDirtyMask
import org.jetbrains.kotlin.backend.common.ir.addDispatchReceiver
import org.jetbrains.kotlin.backend.common.lower.DeclarationIrBuilder
import org.jetbrains.kotlin.descriptors.Modality
import org.jetbrains.kotlin.ir.builders.declarations.addValueParameter
import org.jetbrains.kotlin.ir.builders.declarations.buildFun
import org.jetbrains.kotlin.ir.builders.irBlockBody
import org.jetbrains.kotlin.ir.declarations.IrProperty
import org.jetbrains.kotlin.ir.declarations.IrSimpleFunction
import org.jetbrains.kotlin.ir.declarations.IrValueParameter
import org.jetbrains.kotlin.ir.expressions.IrBody
import org.jetbrains.kotlin.ir.util.defaultType

class RumDirtyMask2Air(
    parent: ClassBoundIrBuilder,
    val dirtyMask: RumDirtyMask
) : ClassBoundIrBuilder(parent) {

    fun toAir(): AirDirtyMask = with(dirtyMask) {
        val name = Strings.ADAPTIVE_DIRTY_FUN.toNameWithPostfix(dirtyMask.index)
        val property = addIrProperty(name, irBuiltIns.longType, true, irConst(0))

        return AirDirtyMask(
            dirtyMask,
            property,
            invalidate(property),
        )
    }

    private fun RumDirtyMask.invalidate(property: IrProperty): IrSimpleFunction =
        irFactory
            .buildFun {
                name = Strings.ADAPTIVE_INVALIDATE_FUN.toNameWithPostfix(index)
                returnType = irBuiltIns.unitType
                modality = Modality.OPEN
            }.also { function ->

                function.parent = irClass

                val receiver = function.addDispatchReceiver {
                    type = irClass.defaultType
                }

                val mask = function.addValueParameter {
                    name = Names.ADAPTIVE_INVALIDATE_MASK_ARG
                    type = irBuiltIns.longType
                }

                function.body = invalidateBody(function, property, receiver, mask)

                irClass.declarations += function
            }

    private fun invalidateBody(function: IrSimpleFunction, property: IrProperty, receiver: IrValueParameter, mask: IrValueParameter): IrBody =
        DeclarationIrBuilder(irContext, function.symbol).irBlockBody {
            +irSetValue(
                property,
                irOr(
                    irGetValue(property, irGet(receiver)),
                    irGet(mask)
                ),
                receiver = irGet(receiver)
            )
        }
}
