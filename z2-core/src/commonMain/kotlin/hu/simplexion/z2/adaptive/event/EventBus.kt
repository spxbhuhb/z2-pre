package hu.simplexion.z2.adaptive.event

import hu.simplexion.z2.util.Z2Handle

class EventBus(
    val handle : Z2Handle,
    val listeners : MutableList<Z2EventListener> = mutableListOf()
)
