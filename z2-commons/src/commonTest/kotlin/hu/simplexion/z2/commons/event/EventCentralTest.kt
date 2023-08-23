package hu.simplexion.z2.commons.event

import hu.simplexion.z2.commons.util.nextHandle
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

        val listener = AnonymousEventListener { value++ }

        EventCentral.attach(busHandle, listener)
        assertEquals(1, busCount)
        assertEquals(1, listenerCount)

        EventCentral.fire(BasicEvent(busHandle))
        assertTrue(value == 1)

        EventCentral.detach(busHandle, listener)
        assertEquals(0, busCount)
    }

    @Test
    fun attachDetachOneBusMulti() {
        var value = 0

        val busHandle = nextHandle()

        val listener1 = AnonymousEventListener { value++ }
        val listener2 = AnonymousEventListener { value++ }

        EventCentral.attach(busHandle, listener1)
        assertEquals(1, busCount)
        assertEquals(1, listenerCount)

        EventCentral.attach(busHandle, listener2)
        assertEquals(1, busCount)
        assertEquals(2, listenerCount)

        EventCentral.fire(BasicEvent(busHandle))

        assertTrue(value == 2)

        EventCentral.detach(busHandle, listener1)
        assertEquals(1, busCount)
        assertEquals(1, listenerCount)

        EventCentral.detach(busHandle, listener2)
        assertEquals(0, busCount)
    }

    @Test
    fun attachDetachMultiBusMulti() {
        var value = 0

        val busHandle1 = nextHandle()
        val busHandle2 = nextHandle()

        val listener1 = AnonymousEventListener { value++ }
        val listener2 = AnonymousEventListener { value++ }

        EventCentral.attach(busHandle1, listener1)
        assertEquals(1, busCount)
        assertEquals(1, listenerCount)

        EventCentral.attach(busHandle2, listener2)
        assertEquals(2, busCount)
        assertEquals(1, listenerCount)

        EventCentral.fire(BasicEvent(busHandle1))
        assertTrue(value == 1)

        EventCentral.fire(BasicEvent(busHandle2))
        assertTrue(value == 2)

        EventCentral.detach(busHandle1, listener1)
        assertEquals(1, busCount)
        assertEquals(1, listenerCount)

        EventCentral.detach(busHandle2, listener2)
        assertEquals(0, busCount)
    }
}