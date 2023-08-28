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
        return runtime.add(serviceContext, registration)
    }

    override suspend fun update(registration: WorkerRegistration) {
        ensureTechnicalAdmin()
        validate(registration)
        runtime.update(serviceContext, registration)
    }

    override suspend fun remove(registration: UUID<WorkerRegistration>) {
        ensureTechnicalAdmin()
        runtime.remove(serviceContext, registration)
    }

    override suspend fun list(): List<WorkerRegistration> {
        ensureTechnicalAdmin()
        return runtime.list()
    }

    override suspend fun enable(registration: UUID<WorkerRegistration>) {
        ensureTechnicalAdmin()
        runtime.setEnabled(serviceContext, registration, true)
    }

    override suspend fun disable(registration: UUID<WorkerRegistration>) {
        ensureTechnicalAdmin()
        runtime.setEnabled(serviceContext, registration, false)
    }

    override suspend fun copy(registration: UUID<WorkerRegistration>): UUID<WorkerRegistration> {
        TODO("Not yet implemented")
    }

    override suspend fun notify(registration: UUID<WorkerRegistration>, notification: UUID<*>) {
        TODO("Not yet implemented")
    }

}