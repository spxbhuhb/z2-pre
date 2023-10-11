package hu.simplexion.z2.worker.api

import hu.simplexion.z2.commons.util.UUID
import hu.simplexion.z2.service.Service
import hu.simplexion.z2.worker.model.WorkerProvider
import hu.simplexion.z2.worker.model.WorkerRegistration

interface WorkerApi : Service {

    suspend fun add(registration: WorkerRegistration) : UUID<WorkerRegistration>

    suspend fun update(registration : WorkerRegistration)

    suspend fun remove(registration : UUID<WorkerRegistration>)

    suspend fun list() : List<WorkerRegistration>

    suspend fun enable(registration : UUID<WorkerRegistration>)

    suspend fun disable(registration : UUID<WorkerRegistration>)

    suspend fun hasRegistrationFor(providerUuid: UUID<WorkerProvider>) =
        list().any { it.provider == providerUuid }

}