package hu.simplexion.z2.adaptive.worker

import hu.simplexion.z2.adaptive.AdaptiveBridge

interface AdaptiveWorker<BT> {
    fun create()
    fun mount(bridge: AdaptiveBridge<BT>)
    fun dispose()
    fun unmount(bridge: AdaptiveBridge<BT>)
}