package hu.simplexion.z2.worker

import hu.simplexion.z2.commons.util.UUID
import hu.simplexion.z2.worker.model.BackgroundWorker
import hu.simplexion.z2.worker.model.WorkerProvider
import hu.simplexion.z2.worker.model.WorkerRegistration

class TestProvider : WorkerProvider {

    override val uuid
        get() = UUID

    override fun newBackgroundWorker(registration: WorkerRegistration): BackgroundWorker {
        return TestWorker(registration)
    }

    companion object {
        val UUID = UUID<WorkerProvider>("9c028a5d-7368-45aa-9765-412f758a9c30")
    }

}