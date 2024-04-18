package hu.simplexion.z2.application.model

import hu.simplexion.z2.auth.model.Principal
import hu.simplexion.z2.auth.model.Role
import hu.simplexion.z2.schematic.Schematic
import hu.simplexion.z2.schematic.SchematicCompanion

class ApplicationSettings : Schematic<ApplicationSettings>() {

    var applicationUuid by uuid<Principal>()

    var securityOfficerRole by schematic<Role>()

    var technicalAdminRole by schematic<Role>()

    var mode by enum<ApplicationMode>()

    var url by string()

    companion object : SchematicCompanion<ApplicationSettings> {
        const val APPLICATION_UUID_KEY = "APPLICATION_UUID"
        const val APPLICATION_MODE = "APPLICATION_MODE"
        const val APPLICATION_URL = "APPLICATION_URL"
        const val SECURITY_OFFICER_UUID_KEY = "SECURITY_OFFICER_UUID"
        const val SECURITY_OFFICER_NAME_KEY = "SECURITY_OFFICER_NAME"
        const val SECURITY_OFFICER_ROLE_UUID_KEY = "SECURITY_OFFICER_ROLE_UUID"
        const val SECURITY_OFFICER_ROLE_NAME_KEY = "SECURITY_OFFICER_ROLE_NAME"
        const val TECHNICAL_ADMIN_ROLE_UUID_KEY = "TECHNICAL_ADMIN_ROLE_UUID"
        const val TECHNICAL_ADMIN_ROLE_NAME_KEY = "TECHNICAL_ADMIN_ROLE_NAME"
        const val ANONYMOUS_UUID_KEY = "ANONYMOUS_UUID"
        const val ANONYMOUS_NAME_KEY = "ANONYMOUS_NAME"
    }
}