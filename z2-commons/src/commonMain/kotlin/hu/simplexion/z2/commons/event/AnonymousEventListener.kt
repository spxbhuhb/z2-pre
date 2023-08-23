package hu.simplexion.z2.commons.event

import hu.simplexion.z2.commons.util.nextHandle

class AnonymousEventListener(
    val listenerFun : (event : Z2Event) -> Unit
) : Z2EventListener {

    override val listenerHandle = nextHandle()

    override fun accept(event: Z2Event) {
        listenerFun(event)
    }

}