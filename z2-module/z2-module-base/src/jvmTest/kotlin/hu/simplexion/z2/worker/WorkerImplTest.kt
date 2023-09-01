package hu.simplexion.z2.worker

import hu.simplexion.z2.commons.util.UUID
import hu.simplexion.z2.testing.integratedWithSo
import hu.simplexion.z2.worker.impl.WorkerImpl.Companion.workerImpl
import hu.simplexion.z2.worker.model.WorkerRegistration
import hu.simplexion.z2.worker.model.WorkerStatus
import hu.simplexion.z2.worker.runtime.WorkerRuntime
import hu.simplexion.z2.worker.runtime.WorkerRuntime.Companion.workerRuntime
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class WorkerImplTest {

    @Test
    fun testProvider() {
        integratedWithSo { test, so ->

            workerRuntime += TestProvider()

            workerImpl(so).list().also {
                assertTrue(it.isEmpty())
            }

            val registration = WorkerRegistration().also {
                it.uuid = UUID() // FIXME uuid should be skipped when validating for create
                it.provider = TestProvider.UUID
                it.name = "Test Worker"
                it.enabled = false
            }

            val workerUuid = workerImpl(so).add(registration)

            workerImpl(so).list().assertOne(workerUuid, "Test Worker", enabled = false, status = WorkerStatus.Stopped)
            assertTrue(TestWorker.state[workerUuid] == null)

            workerImpl(so).enable(workerUuid)

            workerImpl(so).list().assertOne(workerUuid, "Test Worker", enabled = true, status = WorkerStatus.Running)
            assertTrue(TestWorker.state[workerUuid] == false)

            workerImpl(so).disable(workerUuid)

            workerImpl(so).list().assertOne(workerUuid, "Test Worker", enabled = false, status = WorkerStatus.Stopped)
            assertTrue(TestWorker.state[workerUuid] == false)
        }
    }

    fun List<WorkerRegistration>.assertOne(
        workerUuid : UUID<WorkerRegistration>, name : String, enabled : Boolean, status : WorkerStatus
    ) {
        assertEquals(1, size)
        val i = first()
        assertEquals(workerUuid, workerUuid)
        assertEquals(TestProvider.UUID, i.provider)
        assertEquals(name, i.name)
        assertEquals(enabled, i.enabled)
        assertEquals(status, i.status)
    }

}