package hu.simplexion.z2.commons.event

import hu.simplexion.z2.commons.util.Z2Handle

class EventBus(
    val handle : Z2Handle,
    val listeners : MutableList<Z2EventListener> = mutableListOf()
)
