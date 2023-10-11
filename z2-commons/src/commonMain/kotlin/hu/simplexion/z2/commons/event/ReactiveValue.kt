package hu.simplexion.z2.commons.event

import hu.simplexion.z2.commons.util.Z2Handle

class ReactiveValue<V>(
    handle: Z2Handle,
    val builder: (curVal: V?) -> V
) {

    var curVal: V? = null
    val listener = AnonymousEventListener(handle) { curVal = builder(curVal) }

    init {
        EventCentral.attach(listener)
    }

    fun dispose() {
        EventCentral.detach(listener)
    }

}