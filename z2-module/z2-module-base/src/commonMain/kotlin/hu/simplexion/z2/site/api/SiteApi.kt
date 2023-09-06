package hu.simplexion.z2.site.api

import hu.simplexion.z2.service.runtime.Service

interface SiteApi : Service {

    suspend fun siteUrl() : String

}