package hu.simplexion.z2.deprecated.event

import hu.simplexion.z2.util.Z2Handle
import hu.simplexion.z2.util.nextHandle

class AnonymousEventListener(
    override val busHandle : Z2Handle,
    val listenerFun : (event : Z2Event) -> Unit
) : Z2EventListener {

    override val listenerHandle = nextHandle()

    override fun accept(event: Z2Event) {
        listenerFun(event)
    }

}