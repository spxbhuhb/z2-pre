package hu.simplexion.z2.browser.immaterial.schematic

import hu.simplexion.z2.browser.html.Z2
import hu.simplexion.z2.deprecated.event.AnonymousEventListener
import hu.simplexion.z2.deprecated.event.EventCentral
import hu.simplexion.z2.deprecated.event.Z2Event
import hu.simplexion.z2.util.Z2Handle
import hu.simplexion.z2.util.nextHandle

inline fun <T> state(builder : () -> T) = State(builder())

fun <T> Z2.attach(state : State<T>, renderFun : Z2.(value : T) -> Unit) : Z2 {
    listeners += AnonymousEventListener(state.handle) {
        clear()
        @Suppress("UNCHECKED_CAST")
        renderFun((it as StateChange<T>).state.value)
    }.also {
        EventCentral.attach(it)
    }
    renderFun(state.value)
    return this
}

class StateChange<T>(
    override val busHandle: Z2Handle,
    val state : State<T>
) : Z2Event

class State<T>(
    initialValue : T
) {
    val handle = nextHandle()
    var value = initialValue
        set(newValue) {
            field = newValue
            EventCentral.fire(StateChange(handle, this))
        }
}
