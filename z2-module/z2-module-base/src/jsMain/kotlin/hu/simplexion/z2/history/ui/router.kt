package hu.simplexion.z2.history.ui

import hu.simplexion.z2.browser.html.Z2Builder
import hu.simplexion.z2.browser.routing.NavRouter

@Suppress("unused")
object historiesRouter : NavRouter() {
    override val label = strings.histories
    override val icon = icons.history

    val overview by render(strings.overview, icons.overview) { list() }
    val security by render(strings.security, icons.security) { list() }
    val technical by render(strings.technical, icons.technical) { list() }
    val error by render(strings.error, icons.error) { list() }
    val business by render(strings.business, icons.business) { list() }

    override val default: Z2Builder = { list() }
}