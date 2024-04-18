package hu.simplexion.z2.worker.runtime

import hu.simplexion.z2.util.UUID
import hu.simplexion.z2.worker.model.WorkerRegistration

class WorkerRuntimeResponse(
    val registration : WorkerRegistration? = null,
    val registrationUuid : UUID<WorkerRegistration>? = null,
    val registrationList : List<WorkerRegistration>? = null,
    val exception : Exception? = null
)