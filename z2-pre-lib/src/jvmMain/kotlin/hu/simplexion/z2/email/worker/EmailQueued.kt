package hu.simplexion.z2.email.worker

import hu.simplexion.z2.deprecated.event.Z2Event
import hu.simplexion.z2.email.emailBusHandle
import hu.simplexion.z2.email.model.Email
import hu.simplexion.z2.util.UUID
import hu.simplexion.z2.util.Z2Handle

class EmailQueued(
    val uuid: UUID<Email>
) : Z2Event {
    override val busHandle: Z2Handle
        get() = emailBusHandle
}