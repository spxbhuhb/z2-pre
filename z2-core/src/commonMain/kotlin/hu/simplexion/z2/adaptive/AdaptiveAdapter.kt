/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.z2.adaptive

interface AdaptiveAdapter<BT> : Adaptive {

    val rootBridge: AdaptiveBridge<BT>

    val trace : Boolean

    fun createPlaceholder(): AdaptiveBridge<BT>

    fun newId(): Long

    fun trace(fragment: AdaptiveFragment<BT>, point: String, data : String) {
        // FIXME should we escape the data string? think about security
        val name = (fragment::class.simpleName ?: "").padEnd(30)
        val id = fragment.id.toString().padStart(4, ' ')
        println("[ $name @ $id ] ${point.padEnd(20)}  |  $data")
    }
}