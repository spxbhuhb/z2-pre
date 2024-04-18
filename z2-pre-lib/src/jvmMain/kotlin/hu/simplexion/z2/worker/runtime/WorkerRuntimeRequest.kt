package hu.simplexion.z2.worker.runtime

import hu.simplexion.z2.auth.model.Principal
import hu.simplexion.z2.util.UUID
import hu.simplexion.z2.worker.model.WorkerProvider
import hu.simplexion.z2.worker.model.WorkerRegistration
import kotlinx.coroutines.channels.Channel

class WorkerRuntimeRequest(
    val executor: UUID<Principal>,
    val type : WorkerRuntimeMessageType,
    val registration : WorkerRegistration? = null,
    val registrationUuid : UUID<WorkerRegistration>? = null,
    val provider : WorkerProvider? = null,
    val responseChannel : Channel<WorkerRuntimeResponse>? = null
)