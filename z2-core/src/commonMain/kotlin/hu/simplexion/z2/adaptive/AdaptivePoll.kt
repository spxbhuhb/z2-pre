package hu.simplexion.z2.adaptive

import hu.simplexion.z2.util.Lock
import hu.simplexion.z2.util.use
import kotlinx.coroutines.*

class AdaptivePoll<BT>(
    val pollFunction: AdaptiveSupportFunction<BT>,
    val indexInState: Int,
    val interval: Long? = null,
    var repeat: Int? = null
) : AdaptiveWorker<BT> {

    var scope: CoroutineScope? = null

    override fun create() {

    }

    override fun mount(bridge: AdaptiveBridge<BT>) {
        scope = CoroutineScope(Dispatchers.Default).apply {
            launch {
                while (isActive) {
                    pollLock.use {
                        val value = pollFunction.invoke(pollFunction.declaringFragment)
                        pollFunction.declaringFragment.setStateVariable(indexInState, value)
                    }
                    interval?.let { delay(interval) }
                    repeat?.let { if (it > 0) repeat = (it - 1) else cancel() }
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
        return "AdaptivePoll($pollFunction, indexInState=$indexInState, interval=$interval, repeat=$repeat)"
    }

    companion object {
        val pollLock = Lock()
    }
}