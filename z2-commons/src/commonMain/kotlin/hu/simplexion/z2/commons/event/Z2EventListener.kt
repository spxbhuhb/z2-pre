package hu.simplexion.z2.commons.event

import hu.simplexion.z2.commons.util.Z2Handle

interface Z2EventListener {
    val busHandle : Z2Handle
    val listenerHandle : Z2Handle
    fun accept(event : Z2Event)
}