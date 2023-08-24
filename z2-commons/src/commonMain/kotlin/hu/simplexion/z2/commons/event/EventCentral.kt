package hu.simplexion.z2.commons.event

import hu.simplexion.z2.commons.util.Lock
import hu.simplexion.z2.commons.util.Z2Handle
import hu.simplexion.z2.commons.util.use

object EventCentral {

    var trace = false

    private val mutex = Lock()

    val buses = mutableMapOf<Z2Handle, EventBus>()

    fun fire(event: Z2Event) {
        if (trace) println("EventCentral.fire $event")

        val bus = mutex.use { buses[event.busHandle] } ?: return

        for (listener in bus.listeners) {
            listener.accept(event)
        }
    }

    fun attach(handle: Z2Handle, listenerFun: (event: Z2Event) -> Unit) {
        if (trace) println("EventCentral.attach bus: $handle")
        attach(handle, AnonymousEventListener(listenerFun))
    }

    fun attach(busHandle: Z2Handle, listener: Z2EventListener) {
        if (trace) println("EventCentral.attach bus: $busHandle listener: ${listener.listenerHandle}")
        mutex.use {
            val bus = buses.getOrPut(busHandle) { EventBus(busHandle) }
            bus.listeners += listener
        }
    }

    fun detach(busHandle: Z2Handle, listener: Z2EventListener) {
        if (trace) println("EventCentral.attach bus: $busHandle listener: ${listener.listenerHandle}")

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