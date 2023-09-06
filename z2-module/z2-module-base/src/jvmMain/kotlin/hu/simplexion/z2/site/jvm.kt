package hu.simplexion.z2.site

import hu.simplexion.z2.exposed.implementations
import hu.simplexion.z2.site.impl.SiteImpl.Companion.siteImpl

fun siteJvm() {
    siteCommon()
    implementations(siteImpl)
}