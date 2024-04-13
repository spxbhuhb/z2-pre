package hu.simplexion.z2.application.api

import hu.simplexion.z2.application.model.ApplicationSettings
import hu.simplexion.z2.services.Service

interface ApplicationApi : Service {

    suspend fun applicationSettings(): ApplicationSettings

}