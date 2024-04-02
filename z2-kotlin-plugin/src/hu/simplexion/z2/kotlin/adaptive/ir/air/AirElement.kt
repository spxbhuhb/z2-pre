package hu.simplexion.z2.kotlin.adaptive.ir.air

import hu.simplexion.z2.kotlin.adaptive.ir.air.visitors.AirElementVisitor
import hu.simplexion.z2.kotlin.adaptive.ir.air.visitors.DumpAirTreeVisitor

interface AirElement {

    fun <R, D> accept(visitor: AirElementVisitor<R, D>, data: D): R

    fun <D> acceptChildren(visitor: AirElementVisitor<Unit, D>, data: D)

    fun dump(): String {
        val out = StringBuilder()
        this.accept(DumpAirTreeVisitor(out), null)
        return out.toString()
    }

}