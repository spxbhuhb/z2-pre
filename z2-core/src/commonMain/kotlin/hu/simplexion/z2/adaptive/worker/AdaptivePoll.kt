package hu.simplexion.z2.adaptive.worker

import hu.simplexion.z2.adaptive.AdaptiveStateVariableBinding
import hu.simplexion.z2.adaptive.AdaptiveSupportFunction
import kotlinx.coroutines.*
import kotlin.time.Duration

class AdaptivePoll<VT>(
    val binding: AdaptiveStateVariableBinding<VT>,
    val interval: Duration
) : AdaptiveWorker {

    val pollFunction = AdaptiveSupportFunction(
        binding.owner,
        binding.owner,
        binding.supportFunction
    )

    var scope: CoroutineScope? = null

    override fun replaces(other: AdaptiveWorker): Boolean =
        other is AdaptivePoll<*> && other.binding == this.binding

    override fun start() {
        scope = CoroutineScope(binding.owner.adapter.dispatcher).apply {
            launch {
                while (isActive) {

                    try {
                        @Suppress("UNCHECKED_CAST")
                        binding.value = pollFunction.invokeSuspend() as VT
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
        return "AdaptivePoll($binding, $interval)"
    }

}