package hu.simplexion.z2.worker.model

import hu.simplexion.z2.commons.util.UUID

interface BackgroundWorker {

    val registration : UUID<WorkerRegistration>

    suspend fun start()
    suspend fun stop()

}