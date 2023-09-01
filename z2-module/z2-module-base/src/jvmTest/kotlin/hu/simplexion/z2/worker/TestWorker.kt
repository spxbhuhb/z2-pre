package hu.simplexion.z2.worker

import hu.simplexion.z2.commons.util.UUID
import hu.simplexion.z2.worker.model.BackgroundWorker
import hu.simplexion.z2.worker.model.WorkerRegistration
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import java.util.concurrent.ConcurrentHashMap

class TestWorker(
    override val registration: UUID<WorkerRegistration>
) : BackgroundWorker {

    companion object {
        val state = ConcurrentHashMap<UUID<WorkerRegistration>, Boolean>()
    }

    init {
        state[registration] = false
    }

    override suspend fun run(job : Job) {
        state[registration] = true
        while (job.isActive) {
            try {
                delay(10)
            } catch (ex : CancellationException) {
                // nothing to do here
            }
        }
        state[registration] = false
    }

}