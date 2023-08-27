package hu.simplexion.z2.worker.impl

import hu.simplexion.z2.auth.context.ensureTechnicalAdmin
import hu.simplexion.z2.commons.util.UUID
import hu.simplexion.z2.history.util.technicalHistory
import hu.simplexion.z2.schematic.runtime.validate
import hu.simplexion.z2.service.runtime.ServiceImpl
import hu.simplexion.z2.worker.api.WorkerApi
import hu.simplexion.z2.worker.model.WorkerRegistration
import hu.simplexion.z2.worker.runtime.WorkerRuntime
import hu.simplexion.z2.worker.runtime.WorkerRuntime.start
import hu.simplexion.z2.worker.runtime.WorkerRuntime.stop
import hu.simplexion.z2.worker.table.WorkerRegistrationTable.Companion.workerRegistrationTable

class WorkerImpl : WorkerApi, ServiceImpl<WorkerImpl> {

    companion object {
        val workerImpl = WorkerImpl()
        val runtime = WorkerRuntime
    }

    override suspend fun add(registration: WorkerRegistration): UUID<WorkerRegistration> {
        ensureTechnicalAdmin()
        validate(registration)
        technicalHistory(registration)

        registration.uuid = workerRegistrationTable.insert(registration)

        runtime += registration

        return registration.uuid
    }

    override suspend fun update(registration: WorkerRegistration) {
        ensureTechnicalAdmin()
        validate(registration)
        technicalHistory(registration)

        if (registration.enabled) {
            start(registration)
        } else {
            stop(registration)
        }

        workerRegistrationTable.update(registration.uuid, registration)
    }

    override suspend fun remove(registration: UUID<WorkerRegistration>) {
        ensureTechnicalAdmin()
        technicalHistory(registration)

        workerRegistrationTable.remove(registration)
    }

    override suspend fun list(): List<WorkerRegistration> {
        ensureTechnicalAdmin()
        return workerRegistrationTable.list()
    }

    override suspend fun enable(registration: UUID<WorkerRegistration>) {
        ensureTechnicalAdmin()
        technicalHistory(registration)
        workerRegistrationTable.setEnabled(registration, true)
        runtime.start(registration)
    }

    override suspend fun disable(registration: UUID<WorkerRegistration>) {
        ensureTechnicalAdmin()
        technicalHistory(registration)
        workerRegistrationTable.setEnabled(registration, true)
        runtime.stop(registration)
    }

    override suspend fun copy(registration: UUID<WorkerRegistration>): UUID<WorkerRegistration> {
        TODO("Not yet implemented")
    }

    override suspend fun notify(registration: UUID<WorkerRegistration>, notification: UUID<*>) {
        TODO("Not yet implemented")
    }

}