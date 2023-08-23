package hu.simplexion.z2.commons.event

import hu.simplexion.z2.commons.util.Lock
import hu.simplexion.z2.commons.util.Z2Handle
import hu.simplexion.z2.commons.util.use

object EventCentral {

    private val mutex = Lock()

    val buses = mutableMapOf<Z2Handle, EventBus>()

    fun fire(event: Z2Event) {
        val bus = mutex.use { buses[event.busHandle] } ?: return

        for (listener in bus.listeners) {
            listener.accept(event)
        }
    }

    fun attach(handle: Z2Handle, listenerFun: (event: Z2Event) -> Unit) {
        attach(handle, AnonymousEventListener(listenerFun))
    }

    fun attach(busHandle: Z2Handle, listener: Z2EventListener) {
        mutex.use {
            val bus = buses.getOrPut(busHandle) { EventBus(busHandle) }
            bus.listeners += listener
        }
    }

    fun detach(busHandle: Z2Handle, listener: Z2EventListener) {
        mutex.use {
            buses[busHandle]?.let {
                it.listeners -= listener
                if (it.listeners.isEmpty()) buses.remove(it.handle)
            }
        }
    }

    fun detachAll(busHandle: Z2Handle) {
        mutex.use {
            buses.remove(busHandle)
        }
    }
}