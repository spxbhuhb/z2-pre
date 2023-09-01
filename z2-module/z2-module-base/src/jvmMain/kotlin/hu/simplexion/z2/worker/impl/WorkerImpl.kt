package hu.simplexion.z2.worker.impl

import hu.simplexion.z2.auth.context.account
import hu.simplexion.z2.auth.context.ensureTechnicalAdmin
import hu.simplexion.z2.commons.util.UUID
import hu.simplexion.z2.schematic.runtime.validate
import hu.simplexion.z2.service.runtime.ServiceImpl
import hu.simplexion.z2.worker.api.WorkerApi
import hu.simplexion.z2.worker.model.WorkerRegistration
import hu.simplexion.z2.worker.runtime.WorkerRuntime
import hu.simplexion.z2.worker.runtime.WorkerRuntime.Companion.workerRuntime
import hu.simplexion.z2.worker.runtime.WorkerRuntimeMessageType

class WorkerImpl : WorkerApi, ServiceImpl<WorkerImpl> {

    companion object {
        val workerImpl = WorkerImpl()
    }

    override suspend fun add(registration: WorkerRegistration): UUID<WorkerRegistration> {
        ensureTechnicalAdmin()
        validate(registration)

        workerRuntime.sendAndWait(
            serviceContext.account,
            WorkerRuntimeMessageType.AddRegistration,
            registration
        ).also {
            return checkNotNull(it.registrationUuid)
        }
    }

    override suspend fun update(registration: WorkerRegistration) {
        ensureTechnicalAdmin()
        validate(registration)

        workerRuntime.sendAndWait(
            serviceContext.account,
            WorkerRuntimeMessageType.UpdateRegistration,
            registration
        )
    }

    override suspend fun remove(registration: UUID<WorkerRegistration>) {
        ensureTechnicalAdmin()

        workerRuntime.sendAndWait(
            serviceContext.account,
            WorkerRuntimeMessageType.RemoveRegistration,
            registrationUuid = registration
        )
    }

    override suspend fun list(): List<WorkerRegistration> {
        ensureTechnicalAdmin()

        workerRuntime.sendAndWait(
            serviceContext.account,
            WorkerRuntimeMessageType.ListRegistrations
        ).also {
            return checkNotNull(it.registrationList)
        }
    }

    override suspend fun enable(registration: UUID<WorkerRegistration>) {
        ensureTechnicalAdmin()

        workerRuntime.sendAndWait(
            serviceContext.account,
            WorkerRuntimeMessageType.EnableRegistration,
            registrationUuid = registration
        )
    }

    override suspend fun disable(registration: UUID<WorkerRegistration>) {
        ensureTechnicalAdmin()

        workerRuntime.sendAndWait(
            serviceContext.account,
            WorkerRuntimeMessageType.DisableRegistration,
            registrationUuid = registration
        )
    }

    override suspend fun copy(registration: UUID<WorkerRegistration>): UUID<WorkerRegistration> {
        TODO("Not yet implemented")
    }

    override suspend fun notify(registration: UUID<WorkerRegistration>, notification: UUID<*>) {
        TODO("Not yet implemented")
    }

}