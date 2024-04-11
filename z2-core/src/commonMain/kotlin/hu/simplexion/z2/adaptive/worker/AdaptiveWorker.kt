package hu.simplexion.z2.adaptive.worker

interface AdaptiveWorker {
    fun create() {}
    fun mount() {}
    fun dispose() {}
    fun unmount() {}
}