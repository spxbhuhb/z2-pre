/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.z2.adaptive.testing

import hu.simplexion.z2.adaptive.AdaptiveAdapter
import hu.simplexion.z2.adaptive.AdaptiveBridge
import hu.simplexion.z2.adaptive.AdaptiveFragment

open class AdaptiveTestAdapter : AdaptiveAdapter<TestNode> {

    val fragments = mutableListOf<AdaptiveFragment<TestNode>>()

    var nextId = 1L

    override val trace = true

    final override fun newId(): Long = nextId ++ // This is not thread safe, OK for testing, but beware.

    override val rootBridge = AdaptiveTestBridge(newId())

    val traceEvents = mutableListOf<TraceEvent>()

    init {
        lastTrace = traceEvents
    }

    override fun createPlaceholder(): AdaptiveBridge<TestNode> {
        return AdaptiveTestBridge(newId())
    }

    override fun trace(name: String, id : Long, point: String, vararg data: Any?) {
        // convert the data to string so later changes won't change the content
        traceEvents += TraceEvent(name, id, point, data.map { it.asString() })
    }

    fun Any?.asString(): String =
        when (this) {
            is AdaptiveTestBridge -> this.id.toString()
            else -> this.toString()
        }

    companion object {
        // Unit tests use this property when they run the generated fragment.
        // The trace of the last created adapter is here, unit tests should
        // clear this field before running the generated code.
        var lastTrace: MutableList<TraceEvent> = mutableListOf()

        fun assert(expected: List<TraceEvent>): String {
            return if (expected == lastTrace) {
                "OK"
            } else {
                "Fail:\n==== expected ====\n${expected.joinToString("\n")}\n==== actual ====\n${lastTrace.joinToString("\n")}\n==== code ====\n${lastTrace.joinToString(",\n") { it.toCode() }}"
            }
        }
    }
}