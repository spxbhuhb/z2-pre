package hu.simplexion.z2.site

import hu.simplexion.z2.service.runtime.getService
import hu.simplexion.z2.site.api.SiteApi

val siteService = getService<SiteApi>()

fun siteJs() {
    siteCommon()
}