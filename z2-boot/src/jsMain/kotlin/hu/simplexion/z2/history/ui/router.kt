package hu.simplexion.z2.history.ui

import hu.simplexion.z2.baseIcons
import hu.simplexion.z2.baseStrings
import hu.simplexion.z2.browser.routing.NavRouter

@Suppress("unused")
object historiesRouter : NavRouter(baseStrings.histories, baseIcons.history, default = { list() }) {
    val overview by render(baseStrings.overview, baseIcons.overview) { list() }
    val security by render(baseStrings.security, baseIcons.security) { list() }
    val technical by render(baseStrings.technical, baseIcons.technical) { list() }
    val error by render(baseStrings.error, baseIcons.error) { list() }
    val business by render(baseStrings.business, baseIcons.business) { list() }
}