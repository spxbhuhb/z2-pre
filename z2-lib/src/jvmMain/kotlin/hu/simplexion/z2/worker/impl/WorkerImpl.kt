package hu.simplexion.z2.worker.impl

import hu.simplexion.z2.auth.context.ensureTechnicalAdmin
import hu.simplexion.z2.auth.context.principal
import hu.simplexion.z2.util.UUID
import hu.simplexion.z2.schematic.ensureValid
import hu.simplexion.z2.services.ServiceImpl
import hu.simplexion.z2.worker.api.WorkerApi
import hu.simplexion.z2.worker.model.WorkerRegistration
import hu.simplexion.z2.worker.runtime.WorkerRuntime.Companion.workerRuntime
import hu.simplexion.z2.worker.runtime.WorkerRuntimeMessageType

class WorkerImpl : WorkerApi, ServiceImpl<WorkerImpl> {

    companion object {
        val workerImpl = WorkerImpl().internal
    }

    override suspend fun add(registration: WorkerRegistration): UUID<WorkerRegistration> {
        ensureTechnicalAdmin()
        // FIXME uuid validation during create validate(registration)

        workerRuntime.sendAndWait(
            serviceContext.principal,
            WorkerRuntimeMessageType.AddRegistration,
            registration
        ).also {
            return checkNotNull(it.registrationUuid)
        }
    }

    override suspend fun update(registration: WorkerRegistration) {
        ensureTechnicalAdmin()
        ensureValid(registration)

        workerRuntime.sendAndWait(
            serviceContext.principal,
            WorkerRuntimeMessageType.UpdateRegistration,
            registration
        )
    }

    override suspend fun remove(registration: UUID<WorkerRegistration>) {
        ensureTechnicalAdmin()

        workerRuntime.sendAndWait(
            serviceContext.principal,
            WorkerRuntimeMessageType.RemoveRegistration,
            registrationUuid = registration
        )
    }

    override suspend fun list(): List<WorkerRegistration> {
        ensureTechnicalAdmin()

        workerRuntime.sendAndWait(
            serviceContext.principal,
            WorkerRuntimeMessageType.ListRegistrations
        ).also {
            return checkNotNull(it.registrationList)
        }
    }

    override suspend fun enable(registration: UUID<WorkerRegistration>) {
        ensureTechnicalAdmin()

        workerRuntime.sendAndWait(
            serviceContext.principal,
            WorkerRuntimeMessageType.EnableRegistration,
            registrationUuid = registration
        )
    }

    override suspend fun disable(registration: UUID<WorkerRegistration>) {
        ensureTechnicalAdmin()

        workerRuntime.sendAndWait(
            serviceContext.principal,
            WorkerRuntimeMessageType.DisableRegistration,
            registrationUuid = registration
        )
    }

}