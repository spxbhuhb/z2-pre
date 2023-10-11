package hu.simplexion.z2.worker

class WorkerImplTest {
//
//    @Test
//    fun testProvider() {
//        integratedWithSo { _, so ->
//
//            val runtime = WorkerRuntime()
//            val impl = WorkerImpl().also { it.runtime = runtime }
//
//            runtime += TestProvider()
//
//            impl(so).list().also {
//                assertTrue(it.isEmpty())
//            }
//
//            val registration = WorkerRegistration().also {
//                it.uuid = UUID() // FIXME uuid should be skipped when validating for create
//                it.provider = TestProvider.UUID
//                it.name = "Test Worker"
//                it.enabled = false
//            }
//
//            val workerUuid = impl(so).add(registration)
//
//            impl(so).list().assertOne(workerUuid, "Test Worker", enabled = false, status = WorkerStatus.Stopped)
//            assertTrue(TestWorker.state[workerUuid] == null)
//
//            impl(so).enable(workerUuid)
//
//            impl(so).list().assertOne(workerUuid, "Test Worker", enabled = true, status = WorkerStatus.Running)
//            assertTrue(TestWorker.state[workerUuid] == false)
//
//            impl(so).disable(workerUuid)
//            withTimeout(1000) {
//                while (TestWorker.state[workerUuid] == true) {
//                    delay(10)
//                }
//            }
//
//            impl(so).list().assertOne(workerUuid, "Test Worker", enabled = false, status = WorkerStatus.Stopped)
//            assertTrue(TestWorker.state[workerUuid] == false)
//        }
//    }
//
//    fun List<WorkerRegistration>.assertOne(
//        workerUuid : UUID<WorkerRegistration>, name : String, enabled : Boolean, status : WorkerStatus
//    ) {
//        assertEquals(1, size)
//        val i = first()
//        assertEquals(workerUuid, workerUuid)
//        assertEquals(TestProvider.UUID, i.provider)
//        assertEquals(name, i.name)
//        assertEquals(enabled, i.enabled)
//        assertEquals(status, i.status)
//    }

}