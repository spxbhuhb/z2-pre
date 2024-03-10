/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.z2.kotlin.adaptive.ir.arm

import hu.simplexion.z2.kotlin.adaptive.ir.arm.visitors.ArmElementVisitor
import hu.simplexion.z2.kotlin.adaptive.ir.arm.visitors.DumpArmTreeVisitor

interface ArmElement {

    fun <R, D> accept(visitor: ArmElementVisitor<R, D>, data: D): R

    fun <D> acceptChildren(visitor: ArmElementVisitor<Unit, D>, data: D)

    fun dump(): String {
        val out = StringBuilder()
        this.accept(DumpArmTreeVisitor(out), null)
        return out.toString()
    }

}