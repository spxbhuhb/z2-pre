package hu.simplexion.z2.auth.ui

import hu.simplexion.z2.commons.i18n.LocalizedIconStore
import hu.simplexion.z2.commons.util.UUID

@Suppress("ClassName")
object authIcons : LocalizedIconStore(UUID("c5df98ef-5c7a-40c7-8623-0a8d09184a5d3")) {
    val roles by "recent_actors"
    val accounts by "manage_accounts"
    val securityPolicy by "security"
}