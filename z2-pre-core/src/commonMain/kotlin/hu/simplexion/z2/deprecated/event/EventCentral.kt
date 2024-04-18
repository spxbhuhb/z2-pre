package hu.simplexion.z2.deprecated.event

import hu.simplexion.z2.util.Lock
import hu.simplexion.z2.util.Z2Handle
import hu.simplexion.z2.util.use

object EventCentral {

    var trace = false

    private val mutex = Lock()

    val buses = mutableMapOf<Z2Handle, EventBus>()

    fun fire(event: Z2Event) {
        if (trace) println("[event-central]  fire  event=$event")

        val bus = mutex.use { buses[event.busHandle] } ?: return

        for (listener in bus.listeners) {
            listener.accept(event)
        }
    }

    fun attach(busHandle: Z2Handle, listenerFun: (event: Z2Event) -> Unit) {
        attach(AnonymousEventListener(busHandle, listenerFun))
    }

    fun attach(listener: Z2EventListener) {
        if (trace) println("[event-central]  attach bus=${listener.busHandle} listener=${listener.listenerHandle}")
        mutex.use {
            val bus = buses.getOrPut(listener.busHandle) { EventBus(listener.busHandle) }
            bus.listeners += listener
        }
    }

    fun detach(listener: Z2EventListener) {
        if (trace) println("[event-central]  detach  bus=${listener.busHandle} listener=${listener.listenerHandle}")

        mutex.use {
            buses[listener.busHandle]?.let {
                it.listeners -= listener
                if (it.listeners.isEmpty()) buses.remove(it.handle)
            }
        }
    }

    fun detachAll(listeners: Collection<Z2EventListener>) {
        for (listener in listeners) {
            detach(listener)
        }
    }

    fun detachAll(busHandle: Z2Handle) {
        mutex.use {
            buses.remove(busHandle)
        }
    }
}