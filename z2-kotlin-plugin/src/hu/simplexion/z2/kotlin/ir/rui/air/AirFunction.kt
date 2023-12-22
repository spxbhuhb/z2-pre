package hu.simplexion.z2.kotlin.ir.rui.air

import hu.simplexion.z2.kotlin.ir.rui.ClassBoundIrBuilder
import hu.simplexion.z2.kotlin.ir.rui.rum.RumRenderingStatement
import org.jetbrains.kotlin.ir.declarations.IrSimpleFunction

interface AirFunction : AirElement {

    override val rumElement: RumRenderingStatement
    val irFunction: IrSimpleFunction

    fun toIr(parent: ClassBoundIrBuilder)

}