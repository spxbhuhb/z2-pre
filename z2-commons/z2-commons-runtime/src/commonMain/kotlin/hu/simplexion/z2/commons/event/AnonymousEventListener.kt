package hu.simplexion.z2.commons.event

import hu.simplexion.z2.commons.util.Z2Handle
import hu.simplexion.z2.commons.util.nextHandle

class AnonymousEventListener(
    override val busHandle : Z2Handle,
    val listenerFun : (event : Z2Event) -> Unit
) : Z2EventListener {

    override val listenerHandle = nextHandle()

    override fun accept(event: Z2Event) {
        listenerFun(event)
    }

}