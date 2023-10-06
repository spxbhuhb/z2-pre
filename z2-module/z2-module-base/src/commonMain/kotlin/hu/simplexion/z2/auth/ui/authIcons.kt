package hu.simplexion.z2.auth.ui

import hu.simplexion.z2.commons.localization.icon.LocalizedIconProvider

object authIcons : AuthIcons

interface AuthIcons : LocalizedIconProvider {
    val roles get() = static("recent_actors")
    val accounts get() = static("manage_accounts")
    val securityPolicy get() = static("security")
}