package hu.simplexion.z2.worker

import hu.simplexion.z2.commons.util.UUID
import hu.simplexion.z2.testing.integratedWithSo
import hu.simplexion.z2.worker.impl.WorkerImpl.Companion.workerImpl
import hu.simplexion.z2.worker.model.WorkerRegistration
import hu.simplexion.z2.worker.runtime.WorkerRuntime
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class WorkerImplTest {

    @Test
    fun testProvider() {
        integratedWithSo { test, so ->

            WorkerRuntime += TestProvider()
            WorkerRuntime.start()

            workerImpl(so).list().also {
                assertTrue(it.isEmpty())
            }

            val registration = WorkerRegistration().also {
                it.provider = TestProvider.UUID
                it.name = "Test Worker"
            }

            val workerUuid = workerImpl(so).add(registration)

            workerImpl(so).list().assertOne(workerUuid, "Test Worker", enabled = false, running = false)
            assertTrue(TestWorker.state[workerUuid] == false)

            workerImpl(so).enable(workerUuid)

            workerImpl(so).list().assertOne(workerUuid, "Test Worker", enabled = true, running = true)
            assertTrue(TestWorker.state[workerUuid] == false)

            workerImpl(so).disable(workerUuid)

            workerImpl(so).list().assertOne(workerUuid, "Test Worker", enabled = true, running = true)
            assertTrue(TestWorker.state[workerUuid] == false)
        }
    }

    fun List<WorkerRegistration>.assertOne(
        workerUuid : UUID<WorkerRegistration>, name : String, enabled : Boolean, running : Boolean
    ) {
        assertEquals(1, size)
        val i = first()
        assertEquals(workerUuid, workerUuid)
        assertEquals(TestProvider.UUID, i.provider)
        assertEquals(name, i.name)
        assertEquals(enabled, i.enabled)
        assertEquals(running, i.running)
    }

}