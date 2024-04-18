package hu.simplexion.z2.application

import hu.simplexion.z2.application.model.ApplicationMode
import hu.simplexion.z2.application.model.ApplicationSettings

val isTest
    get() = applicationSettings.mode == ApplicationMode.Test

val securityOfficerRole
    get() = applicationSettings.securityOfficerRole

val technicalAdminRole
    get() = applicationSettings.technicalAdminRole

lateinit var applicationSettings: ApplicationSettings