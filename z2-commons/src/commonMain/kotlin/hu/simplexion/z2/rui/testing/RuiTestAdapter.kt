/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.z2.rui.testing

import hu.simplexion.z2.rui.RuiAdapter
import hu.simplexion.z2.rui.RuiBridge
import hu.simplexion.z2.rui.RuiFragment

open class RuiTestAdapter : RuiAdapter<TestNode> {

    val fragments = mutableListOf<RuiFragment<TestNode>>()

    var nextId = 1

    final override fun newId(): Int = nextId++ // This is not thread safe, OK for testing, but beware.

    override val rootBridge = RuiTestBridge(newId())

    val trace = mutableListOf<String>()

    init {
        lastTrace = trace
    }

    override fun createPlaceholder(): RuiBridge<TestNode> {
        return RuiTestBridge(newId())
    }

    override fun trace(name: String, point: String, vararg data: Any?) {
        trace += "[ ${name.padEnd(30)} ]  ${point.padEnd(20)}  |  ${data.joinToString(" ") { it.asString() }}"
    }

    fun Any?.asString(): String =
        when (this) {
            is RuiTestBridge -> this.id.toString()
            else -> this.toString()
        }

    companion object {
        // Unit tests use this property when they run the generated fragment.
        // The trace of the last created adapter is here, unit tests should
        // clear this field before running the generated code.
        var lastTrace: MutableList<String> = mutableListOf()
    }
}