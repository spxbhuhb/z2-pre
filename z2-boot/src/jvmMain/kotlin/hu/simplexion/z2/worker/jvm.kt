package hu.simplexion.z2.worker

import hu.simplexion.z2.exposed.implementations
import hu.simplexion.z2.exposed.tables
import hu.simplexion.z2.worker.impl.WorkerImpl.Companion.workerImpl
import hu.simplexion.z2.worker.runtime.WorkerRuntime.Companion.workerRuntime
import hu.simplexion.z2.worker.table.WorkerRegistrationTable.Companion.workerRegistrationTable

fun workerJvm() {
    workerCommon()
    tables(workerRegistrationTable)
    implementations(workerImpl)
    workerRuntime.start()
}