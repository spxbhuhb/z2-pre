package hu.simplexion.z2.adaptive.worker

import hu.simplexion.z2.adaptive.AdaptiveBridge
import hu.simplexion.z2.adaptive.AdaptiveFragment
import hu.simplexion.z2.adaptive.AdaptiveSupportFunction
import kotlinx.coroutines.*
import kotlin.time.Duration

class AdaptivePoll<BT>(
    val fragment : AdaptiveFragment<BT>,
    supportFunctionIndex : Int,
    val indexInState: Int,
    val interval: Duration,
    var repeatLimit: Int? = null
) : AdaptiveWorker<BT> {

    val pollFunction = AdaptiveSupportFunction(fragment, fragment, supportFunctionIndex)

    var scope: CoroutineScope? = null

    init {
        fragment.addWorker(this)
    }

    override fun create() {

    }

    override fun mount(bridge: AdaptiveBridge<BT>) {
        scope = CoroutineScope(Dispatchers.Main).apply {
            launch {
                while (isActive) {
                    val value = pollFunction.invokeSuspend(pollFunction.declaringFragment)
                    pollFunction.declaringFragment.setStateVariable(indexInState, value)
                    delay(interval)
                    repeatLimit?.let { if (it > 0) repeatLimit = (it - 1) else cancel() }
                }
            }
        }
    }

    override fun unmount(bridge: AdaptiveBridge<BT>) {
        checkNotNull(scope).cancel()
        scope = null
    }

    override fun dispose() {

    }

    override fun toString(): String {
        return "AdaptivePoll($pollFunction, indexInState=$indexInState, interval=$interval, repeat=$repeatLimit)"
    }

}