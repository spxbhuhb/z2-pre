package hu.simplexion.z2.adaptive.worker

import hu.simplexion.z2.adaptive.AdaptiveStateValueBinding
import hu.simplexion.z2.adaptive.AdaptiveSupportFunction
import kotlinx.coroutines.*
import kotlin.time.Duration

class AdaptivePoll<VT>(
    val stateValueBinding: AdaptiveStateValueBinding<VT>,
    val interval: Duration
) : AdaptiveWorker {

    val pollFunction = AdaptiveSupportFunction(
        stateValueBinding.owner,
        stateValueBinding.owner,
        stateValueBinding.supportFunction
    )

    var scope: CoroutineScope? = null

    override fun replaces(other: AdaptiveWorker): Boolean =
        other is AdaptivePoll<*> && other.stateValueBinding == this.stateValueBinding

    override fun start() {
        scope = CoroutineScope(stateValueBinding.owner.adapter.dispatcher).apply {
            launch {
                while (isActive) {

                    try {
                        @Suppress("UNCHECKED_CAST")
                        stateValueBinding.value = pollFunction.invokeSuspend() as VT
                        delay(interval)

                    } catch (e: AdaptiveWorkerCancel) {
                        cancel()
                        break
                    }
                }
            }
        }
    }

    override fun stop() {
        checkNotNull(scope).cancel()
        scope = null
    }

    override fun toString(): String {
        return "AdaptivePoll($stateValueBinding, $interval)"
    }

}