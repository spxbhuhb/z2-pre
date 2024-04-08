package hu.simplexion.z2.adaptive

interface AdaptiveWorker<BT> {
    fun create()
    fun mount(bridge: AdaptiveBridge<BT>)
    fun dispose()
    fun unmount(bridge: AdaptiveBridge<BT>)
}