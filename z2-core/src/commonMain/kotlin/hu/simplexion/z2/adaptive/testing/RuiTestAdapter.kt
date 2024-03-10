/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.z2.adaptive.testing

import hu.simplexion.z2.adaptive.AdaptiveAdapter
import hu.simplexion.z2.adaptive.AdaptiveBridge
import hu.simplexion.z2.adaptive.AdaptiveFragment

open class AdaptiveTestAdapter : AdaptiveAdapter<TestNode> {

    data class TraceEvent(
        val name: String,
        val point: String,
        val data: List<String>
    ) {

        constructor(name: String, point: String, vararg data: Any?) : this(name, point, data.map { it.toString() })

        override fun toString(): String {
            return "[ ${name.padEnd(30)} ]  ${point.padEnd(20)}  |  ${data.joinToString(" ")}"
        }

        fun toCode(): String {
            return "TraceEvent(\"$name\", \"${point}\", ${data.joinToString(", ") { "\"$it\"" }})"
        }
    }

    val fragments = mutableListOf<AdaptiveFragment<TestNode>>()

    var nextId = 1

    final override fun newId(): Int = nextId ++ // This is not thread safe, OK for testing, but beware.

    override val rootBridge = AdaptiveTestBridge(newId())

    val trace = mutableListOf<TraceEvent>()

    init {
        lastTrace = trace
    }

    override fun createPlaceholder(): AdaptiveBridge<TestNode> {
        return AdaptiveTestBridge(newId())
    }

    override fun trace(name: String, point: String, vararg data: Any?) {
        // convert the data to string so later changes won't change the content
        trace += TraceEvent(name, point, data.map { it.asString() })
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