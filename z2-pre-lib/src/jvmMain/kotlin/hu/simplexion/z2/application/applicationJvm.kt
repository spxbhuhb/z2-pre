package hu.simplexion.z2.application

import hu.simplexion.z2.application.impl.ApplicationImpl.Companion.applicationImpl
import hu.simplexion.z2.application.model.ApplicationMode
import hu.simplexion.z2.application.model.ApplicationSettings
import hu.simplexion.z2.application.model.ApplicationSettings.Companion.APPLICATION_MODE
import hu.simplexion.z2.application.model.ApplicationSettings.Companion.APPLICATION_URL
import hu.simplexion.z2.application.model.ApplicationSettings.Companion.APPLICATION_UUID_KEY
import hu.simplexion.z2.application.model.ApplicationSettings.Companion.SECURITY_OFFICER_ROLE_NAME_KEY
import hu.simplexion.z2.application.model.ApplicationSettings.Companion.SECURITY_OFFICER_ROLE_UUID_KEY
import hu.simplexion.z2.application.model.ApplicationSettings.Companion.TECHNICAL_ADMIN_ROLE_NAME_KEY
import hu.simplexion.z2.application.model.ApplicationSettings.Companion.TECHNICAL_ADMIN_ROLE_UUID_KEY
import hu.simplexion.z2.auth.model.Principal
import hu.simplexion.z2.auth.model.Role
import hu.simplexion.z2.exposed.implementations
import hu.simplexion.z2.setting.dsl.setting
import hu.simplexion.z2.util.UUID

fun applicationJvm() {

    applicationSettings = ApplicationSettings {

        applicationUuid = setting<UUID<Principal>> { APPLICATION_UUID_KEY }.valueOrNull ?: UUID("065ca04c-cd6d-7775-8000-5fa6142b4b7b")

        securityOfficerRole = Role {
            uuid = setting<UUID<Role>> { SECURITY_OFFICER_ROLE_UUID_KEY }.valueOrNull ?: UUID("065ca04e-095c-713b-8000-beb6c07f18cd")
            programmaticName = setting<String> { SECURITY_OFFICER_ROLE_NAME_KEY }.valueOrNull ?: "security-officer"
            displayName = programmaticName
        }

        technicalAdminRole = Role {
            uuid = setting<UUID<Role>> { TECHNICAL_ADMIN_ROLE_UUID_KEY }.valueOrNull ?: UUID("065cca1e-9785-79b6-8000-143a52a8d7e3")
            programmaticName = setting<String> { TECHNICAL_ADMIN_ROLE_NAME_KEY }.valueOrNull ?: "technical-admin"
            displayName = programmaticName
        }

        mode = setting<String> { APPLICATION_MODE }.valueOrNull?.let { ApplicationMode.valueOf(it) } ?: ApplicationMode.Test

        url = setting<String> { APPLICATION_URL }.valueOrNull ?: ""

    }

    implementations(
        applicationImpl
    )
}