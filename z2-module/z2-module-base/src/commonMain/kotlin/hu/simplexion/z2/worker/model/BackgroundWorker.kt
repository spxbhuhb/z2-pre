package hu.simplexion.z2.worker.model

interface BackgroundWorker {

    val registration : WorkerRegistration

    suspend fun start()
    suspend fun stop()

}