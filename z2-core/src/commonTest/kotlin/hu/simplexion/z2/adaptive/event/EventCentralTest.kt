package hu.simplexion.z2.adaptive.event

import hu.simplexion.z2.deprecated.event.AnonymousEventListener
import hu.simplexion.z2.deprecated.event.BasicEvent
import hu.simplexion.z2.deprecated.event.EventCentral
import hu.simplexion.z2.util.nextHandle
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class EventCentralTest {

    val busCount
        get() = EventCentral.buses.size

    val listenerCount
        get() = EventCentral.buses.values.first().listeners.size

    @Test
    fun basic() {
        val busHandle = nextHandle()
        var value = 0

        EventCentral.attach(busHandle) { value++ }
        assertEquals(1, busCount)
        assertEquals(1, listenerCount)

        EventCentral.fire(BasicEvent(busHandle))
        assertTrue(value == 1)

        EventCentral.detachAll(busHandle)
        assertEquals(0, busCount)
    }

    @Test
    fun attachDetachOneBusSingle() {
        var value = 0

        val busHandle = nextHandle()

        val listener = AnonymousEventListener(busHandle) { value++ }

        EventCentral.attach(listener)
        assertEquals(1, busCount)
        assertEquals(1, listenerCount)

        EventCentral.fire(BasicEvent(busHandle))
        assertTrue(value == 1)

        EventCentral.detach(listener)
        assertEquals(0, busCount)
    }

    @Test
    fun attachDetachOneBusMulti() {
        var value = 0

        val busHandle = nextHandle()

        val listener1 = AnonymousEventListener(busHandle) { value++ }
        val listener2 = AnonymousEventListener(busHandle) { value++ }

        EventCentral.attach(listener1)
        assertEquals(1, busCount)
        assertEquals(1, listenerCount)

        EventCentral.attach(listener2)
        assertEquals(1, busCount)
        assertEquals(2, listenerCount)

        EventCentral.fire(BasicEvent(busHandle))

        assertTrue(value == 2)

        EventCentral.detach(listener1)
        assertEquals(1, busCount)
        assertEquals(1, listenerCount)

        EventCentral.detach(listener2)
        assertEquals(0, busCount)
    }

    @Test
    fun attachDetachMultiBusMulti() {
        var value = 0

        val busHandle1 = nextHandle()
        val busHandle2 = nextHandle()

        val listener1 = AnonymousEventListener(busHandle1) { value++ }
        val listener2 = AnonymousEventListener(busHandle2) { value++ }

        EventCentral.attach(listener1)
        assertEquals(1, busCount)
        assertEquals(1, listenerCount)

        EventCentral.attach(listener2)
        assertEquals(2, busCount)
        assertEquals(1, listenerCount)

        EventCentral.fire(BasicEvent(busHandle1))
        assertTrue(value == 1)

        EventCentral.fire(BasicEvent(busHandle2))
        assertTrue(value == 2)

        EventCentral.detach(listener1)
        assertEquals(1, busCount)
        assertEquals(1, listenerCount)

        EventCentral.detach(listener2)
        assertEquals(0, busCount)
    }
}