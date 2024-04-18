package hu.simplexion.z2.application.impl

import hu.simplexion.z2.application.api.ApplicationApi
import hu.simplexion.z2.application.model.ApplicationSettings
import hu.simplexion.z2.auth.context.publicAccess
import hu.simplexion.z2.services.ServiceImpl

class ApplicationImpl : ApplicationApi, ServiceImpl<ApplicationImpl> {

    companion object {
        val applicationImpl = ApplicationImpl().internal
    }

    override suspend fun applicationSettings(): ApplicationSettings {
        publicAccess()

        return hu.simplexion.z2.application.applicationSettings
    }
}