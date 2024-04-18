package hu.simplexion.z2.email.worker

import hu.simplexion.z2.deprecated.event.Z2Event
import hu.simplexion.z2.email.emailBusHandle
import hu.simplexion.z2.email.model.EmailQueueEntry
import hu.simplexion.z2.util.Z2Handle

class EmailQueued(
    val entry: EmailQueueEntry
) : Z2Event {
    override val busHandle: Z2Handle
        get() = emailBusHandle
}