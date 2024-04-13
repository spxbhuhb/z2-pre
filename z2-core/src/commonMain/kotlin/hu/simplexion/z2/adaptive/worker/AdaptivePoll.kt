package hu.simplexion.z2.adaptive.worker

import hu.simplexion.z2.adaptive.AdaptiveStateValueBinding
import hu.simplexion.z2.adaptive.AdaptiveSupportFunction
import kotlinx.coroutines.*
import kotlin.time.Duration

class AdaptivePoll<VT>(
    val stateValueBinding: AdaptiveStateValueBinding<VT>,
    val interval: Duration,
    var repeatLimit: Int? = null
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
                    val value = pollFunction.invokeSuspend()
                    pollFunction.declaringFragment.setStateVariable(stateValueBinding.indexInState, value)
                    delay(interval)
                    repeatLimit?.let { if (it > 0) repeatLimit = (it - 1) else cancel() }
                }
            }
        }
    }

    override fun stop() {
        checkNotNull(scope).cancel()
        scope = null
    }

    override fun toString(): String {
        return "AdaptivePoll($stateValueBinding, $interval, $repeatLimit)"
    }

}