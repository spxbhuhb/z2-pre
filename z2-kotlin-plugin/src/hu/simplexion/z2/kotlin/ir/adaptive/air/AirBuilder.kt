package hu.simplexion.z2.kotlin.ir.adaptive.air

import hu.simplexion.z2.kotlin.ir.adaptive.air.visitors.AirElementVisitor

interface AirBuilder : AirFunction {

    val externalPatch: AirFunction
    val subBuilders: List<AirBuilder>

    override fun <D> acceptChildren(visitor: AirElementVisitor<Unit, D>, data: D) = Unit

}