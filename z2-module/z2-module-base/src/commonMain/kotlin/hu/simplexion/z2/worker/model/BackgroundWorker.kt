package hu.simplexion.z2.worker.model

import hu.simplexion.z2.commons.util.UUID
import kotlinx.coroutines.Job

// TODO think about migrating BackgroundWorker and WorkerInstance
interface BackgroundWorker {

    val registration : UUID<WorkerRegistration>

    suspend fun run(job : Job)

}