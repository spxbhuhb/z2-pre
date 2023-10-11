package hu.simplexion.z2.site.api

import hu.simplexion.z2.service.Service

interface SiteApi : Service {

    suspend fun siteUrl() : String

    suspend fun isTest() : Boolean

    suspend fun testPassword() : String

}