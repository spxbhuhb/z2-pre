@file:Suppress("UNUSED_PARAMETER")

package hu.simplexion.z2.adaptive.worker

import hu.simplexion.z2.adaptive.AdaptiveStateValueBinding
import kotlin.time.Duration

/**
 * Execute [pollFun] once in every [interval]. Adds an [AdaptivePoll] worker to
 * the given component.
 *
 * @param  interval  The interval of execution.
 * @param  pollFun  The poll function to call.
 */
fun <VT> poll(
    interval: Duration,
    default: VT,
    stateValueBinding: AdaptiveStateValueBinding<VT>? = null,
    pollFun: (suspend () -> VT)?
): VT {
    checkNotNull(stateValueBinding)

    stateValueBinding.owner.addWorker(
        AdaptivePoll(stateValueBinding, interval)
    )

    return default
}

/**
 * Cancels the worker by returning from the coroutine. Throws [AdaptiveWorkerCancel].
 */
fun cancelWorker(message : String? = null) : Nothing {
    throw AdaptiveWorkerCancel(message)
}