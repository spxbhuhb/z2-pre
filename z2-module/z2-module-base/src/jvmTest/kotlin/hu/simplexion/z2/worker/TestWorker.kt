package hu.simplexion.z2.worker

import hu.simplexion.z2.commons.util.UUID
import hu.simplexion.z2.worker.model.BackgroundWorker
import hu.simplexion.z2.worker.model.WorkerRegistration
import java.util.concurrent.ConcurrentHashMap

class TestWorker(
    override val registration: WorkerRegistration
) : BackgroundWorker {

    companion object {
        val state = ConcurrentHashMap<UUID<WorkerRegistration>, Boolean>()
    }

    init {
        state[registration.uuid] = false
    }

    override suspend fun start() {
        state[registration.uuid] = true
    }

    override suspend fun stop() {
        state[registration.uuid] = false
    }

}