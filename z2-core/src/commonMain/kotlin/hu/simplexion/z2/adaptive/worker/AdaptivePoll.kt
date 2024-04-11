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

    override fun create() {

    }

    override fun mount() {
        scope = CoroutineScope(stateValueBinding.owner.adapter.dispatcher).apply {
            launch {
                while (isActive) {
                    val value = pollFunction.invokeSuspend(pollFunction.declaringFragment)
                    pollFunction.declaringFragment.setStateVariable(stateValueBinding.indexInState, value)
                    delay(interval)
                    repeatLimit?.let { if (it > 0) repeatLimit = (it - 1) else cancel() }
                }
            }
        }
    }

    override fun unmount() {
        checkNotNull(scope).cancel()
        scope = null
    }

    override fun dispose() {

    }

    override fun toString(): String {
        return "AdaptivePoll($stateValueBinding, interval=$interval, repeatLimit=$repeatLimit)"
    }

}