package hu.simplexion.z2.email.worker

import hu.simplexion.z2.commons.event.Z2Event
import hu.simplexion.z2.commons.util.UUID
import hu.simplexion.z2.commons.util.Z2Handle
import hu.simplexion.z2.email.emailBusHandle
import hu.simplexion.z2.email.model.Email

class EmailQueued(
    val uuid: UUID<Email>
) : Z2Event {
    override val busHandle: Z2Handle
        get() = emailBusHandle
}