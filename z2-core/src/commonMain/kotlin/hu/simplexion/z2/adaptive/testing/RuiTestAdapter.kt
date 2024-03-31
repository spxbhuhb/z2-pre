/*
 * Copyright © 2020-2024, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.z2.adaptive.testing

import hu.simplexion.z2.adaptive.AdaptiveAdapter
import hu.simplexion.z2.adaptive.AdaptiveBridge
import hu.simplexion.z2.adaptive.AdaptiveFragment

open class AdaptiveTestAdapter : AdaptiveAdapter<TestNode> {

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

    override fun trace(fragment: AdaptiveFragment<TestNode>, point: String, data : String) {
        traceEvents += TraceEvent(fragment::class.simpleName ?: "", fragment.id, point, data)
    }

    fun actual(): String =
        traceEvents.joinToString("\n")

    fun expected(expected: List<TraceEvent>) : String =
        expected.joinToString("\n")

    companion object {
        // Unit tests use this property when they run the generated fragment.
        // The trace of the last created adapter is here, unit tests should
        // clear this field before running the generated code.
        var lastTrace: MutableList<TraceEvent> = mutableListOf()

        fun actual(): String =
            lastTrace.joinToString("\n")

        fun expected(expected: List<TraceEvent>) : String =
            expected.joinToString("\n")

        fun toCode() : String =
            lastTrace.joinToString(",\n") { it.toCode() }
        
        fun assert(expected: List<TraceEvent>): String {
            return if (expected == lastTrace) {
                "OK"
            } else {
                "Fail:\n==== expected ====\n${expected.joinToString("\n")}\n==== actual ====\n${lastTrace.joinToString("\n")}\n==== code ====\n${toCode()}"
            }
        }
    }
}