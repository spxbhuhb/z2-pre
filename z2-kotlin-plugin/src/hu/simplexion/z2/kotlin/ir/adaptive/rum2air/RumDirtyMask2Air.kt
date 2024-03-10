package hu.simplexion.z2.kotlin.ir.adaptive.rum2air

import hu.simplexion.z2.kotlin.ir.adaptive.ADAPTIVE_INVALIDATE
import hu.simplexion.z2.kotlin.ir.adaptive.ADAPTIVE_MASK
import hu.simplexion.z2.kotlin.ir.adaptive.ClassBoundIrBuilder
import hu.simplexion.z2.kotlin.ir.adaptive.air.AirDirtyMask
import hu.simplexion.z2.kotlin.ir.adaptive.rum.RumDirtyMask
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
import org.jetbrains.kotlin.name.Name

class RumDirtyMask2Air(
    parent: ClassBoundIrBuilder,
    val dirtyMask: RumDirtyMask
) : ClassBoundIrBuilder(parent) {

    fun toAir(): AirDirtyMask = with(dirtyMask) {
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
                name = Name.identifier(ADAPTIVE_INVALIDATE + index)
                returnType = irBuiltIns.unitType
                modality = Modality.OPEN
            }.also { function ->

                function.parent = irClass

                val receiver = function.addDispatchReceiver {
                    type = irClass.defaultType
                }

                val mask = function.addValueParameter {
                    name = Name.identifier(ADAPTIVE_MASK)
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
