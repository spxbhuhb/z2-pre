@file:Suppress("UnusedReceiverParameter", "UNUSED_PARAMETER")

package hu.simplexion.z2.adaptive.worker

import hu.simplexion.z2.adaptive.Adaptive
import hu.simplexion.z2.util.placeholder
import kotlin.time.Duration

/**
 * Execute [pollFun] once in every [interval]. Adds an [AdaptivePoll] worker to
 * the given component.
 *
 * @param  interval  The interval of execution.
 * @param  repeatLimit  Stop calling [pollFun] after it's been executed [repeatLimit] times.
 * @param  pollFun  The poll function to call.
 */
fun <T> Adaptive.poll(
    interval : Duration,
    repeatLimit: Int? = null,
    pollFun: suspend () -> T
): T {
    placeholder()
}