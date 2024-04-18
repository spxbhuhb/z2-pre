package hu.simplexion.z2.email.worker

import hu.simplexion.z2.util.UUID
import hu.simplexion.z2.worker.model.BackgroundWorker
import hu.simplexion.z2.worker.model.WorkerProvider
import hu.simplexion.z2.worker.model.WorkerRegistration

class EmailWorkerProvider : WorkerProvider {

    companion object {
        val PROVIDER_UUID = UUID<WorkerProvider>("1d7eb633-ff47-48ed-9a82-2f116cff79e1")
    }

    override val uuid
        get() = PROVIDER_UUID

    override fun newBackgroundWorker(registration: WorkerRegistration): BackgroundWorker {
        return EmailWorker(registration.uuid)
    }
}