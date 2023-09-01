package hu.simplexion.z2.email.worker

import hu.simplexion.z2.commons.util.UUID
import hu.simplexion.z2.worker.model.BackgroundWorker
import hu.simplexion.z2.worker.model.WorkerRegistration

class EmailWorker(
    override val registration: UUID<WorkerRegistration>
) : BackgroundWorker {

    override suspend fun start() {
        TODO("Not yet implemented")
    }

    override suspend fun stop() {
        TODO("Not yet implemented")
    }

}