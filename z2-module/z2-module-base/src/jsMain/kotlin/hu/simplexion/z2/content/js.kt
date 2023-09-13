package hu.simplexion.z2.content

import hu.simplexion.z2.content.api.ContentApi
import hu.simplexion.z2.service.runtime.getService

val contentService = getService<ContentApi>()

fun contentJs() {
    contentCommon()
}