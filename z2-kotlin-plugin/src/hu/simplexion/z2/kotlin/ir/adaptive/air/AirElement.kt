package hu.simplexion.z2.kotlin.ir.adaptive.air

import hu.simplexion.z2.kotlin.ir.adaptive.air.visitors.AirElementVisitor
import hu.simplexion.z2.kotlin.ir.adaptive.air.visitors.DumpAirTreeVisitor
import hu.simplexion.z2.kotlin.ir.adaptive.rum.RumElement

interface AirElement {

    val rumElement: RumElement

    fun <R, D> accept(visitor: AirElementVisitor<R, D>, data: D): R

    fun <D> acceptChildren(visitor: AirElementVisitor<Unit, D>, data: D)

    fun dump(): String {
        val out = StringBuilder()
        this.accept(DumpAirTreeVisitor(out), null)
        return out.toString()
    }

}