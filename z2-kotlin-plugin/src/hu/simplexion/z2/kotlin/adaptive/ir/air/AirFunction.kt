package hu.simplexion.z2.kotlin.adaptive.ir.air

import hu.simplexion.z2.kotlin.adaptive.ir.ClassBoundIrBuilder
import hu.simplexion.z2.kotlin.adaptive.ir.air.visitors.AirElementVisitor
import hu.simplexion.z2.kotlin.adaptive.ir.arm.ArmRenderingStatement
import org.jetbrains.kotlin.ir.declarations.IrSimpleFunction

interface AirFunction : AirElement {

    override val armElement: ArmRenderingStatement
    val irFunction: IrSimpleFunction

    fun toIr(parent: ClassBoundIrBuilder)

    override fun <D> acceptChildren(visitor: AirElementVisitor<Unit, D>, data: D) = Unit

}