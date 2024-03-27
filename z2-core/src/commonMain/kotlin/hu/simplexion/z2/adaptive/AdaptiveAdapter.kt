/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.z2.adaptive

interface AdaptiveAdapter<BT> {

    val rootBridge: AdaptiveBridge<BT>

    val trace : Boolean

    fun createPlaceholder(): AdaptiveBridge<BT>

    fun newId(): Long

    fun trace(name: String, id : Long, point: String, vararg data: Any?) {
        // FIXME should we escape the data string? think about security
        println("[ ${name.padEnd(30)} @ $id ] ${point.padEnd(20)}  |  ${data.joinToString(" ") { it.toString() }}")
    }
}