package hu.simplexion.z2.worker.model

import hu.simplexion.z2.util.UUID

interface WorkerProvider {

    val uuid : UUID<WorkerProvider>

    fun newBackgroundWorker(registration: WorkerRegistration) : BackgroundWorker

}