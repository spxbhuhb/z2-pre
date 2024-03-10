package hu.simplexion.z2.kotlin.ir.adaptive.air

import hu.simplexion.z2.kotlin.ir.adaptive.ClassBoundIrBuilder
import hu.simplexion.z2.kotlin.ir.adaptive.air.visitors.AirElementVisitor
import hu.simplexion.z2.kotlin.ir.adaptive.rum.RumRenderingStatement
import org.jetbrains.kotlin.ir.declarations.IrSimpleFunction

interface AirFunction : AirElement {

    override val rumElement: RumRenderingStatement
    val irFunction: IrSimpleFunction

    fun toIr(parent: ClassBoundIrBuilder)

    override fun <D> acceptChildren(visitor: AirElementVisitor<Unit, D>, data: D) = Unit

}