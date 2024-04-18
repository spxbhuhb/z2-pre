package hu.simplexion.z2.application

import hu.simplexion.z2.application.api.ApplicationApi
import hu.simplexion.z2.services.getService

val applicationService = getService<ApplicationApi>()

suspend fun applicationJs() {
    applicationSettings = applicationService.applicationSettings()
}