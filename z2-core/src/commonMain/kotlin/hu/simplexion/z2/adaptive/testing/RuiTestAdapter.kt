/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.z2.adaptive.testing

import hu.simplexion.z2.adaptive.AdaptiveAdapter
import hu.simplexion.z2.adaptive.AdaptiveBridge
import hu.simplexion.z2.adaptive.AdaptiveFragment

open class AdaptiveTestAdapter : AdaptiveAdapter<TestNode> {

    class TraceEvent(
        val name: String,
        val point: String,
        val data: List<String>
    ) {

        constructor(name: String, point: String, vararg data: Any?) : this(name, point, data.map { it.toString() })

        override fun toString(): String {
            return "[ ${name.padEnd(30)} ]  ${point.padEnd(20)}  |  ${data.joinToString(" ")}"
        }

        fun toCode(): String {
            val nameOrRoot = if (name.startsWith("AdaptiveRoot")) "<root>" else name
            return "TraceEvent(\"$nameOrRoot\", \"${point}\", ${data.joinToString(", ") { "\"$it\"" }})"
        }

        override fun equals(other: Any?): Boolean {
            if (other == null) return false
            if (other !is TraceEvent) return false
            if (other.point != this.point) return false
            if (other.data != this.data) return false
            if (other.name == this.name) return true

            if (this.name == "<root>" && other.name.startsWith("AdaptiveRoot")) return true
            if (other.name == "<root>" && this.name.startsWith("AdaptiveRoot")) return true

            return false
        }
    }

    val fragments = mutableListOf<AdaptiveFragment<TestNode>>()

    var nextId = 1

    override val trace = true

    final override fun newId(): Int = nextId ++ // This is not thread safe, OK for testing, but beware.

    override val rootBridge = AdaptiveTestBridge(newId())

    val traceEvents = mutableListOf<TraceEvent>()

    init {
        lastTrace = traceEvents
    }

    override fun createPlaceholder(): AdaptiveBridge<TestNode> {
        return AdaptiveTestBridge(newId())
    }

    override fun trace(name: String, point: String, vararg data: Any?) {
        // convert the data to string so later changes won't change the content
        traceEvents += TraceEvent(name, point, data.map { it.asString() })
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