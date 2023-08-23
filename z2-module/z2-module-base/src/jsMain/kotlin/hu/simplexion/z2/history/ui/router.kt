package hu.simplexion.z2.history.ui

import hu.simplexion.z2.browser.html.Z2Builder
import hu.simplexion.z2.browser.routing.NavRouter

@Suppress("unused")
object historiesRouter : NavRouter() {
    override val label = historyStrings.histories
    override val icon = historyIcons.history

    val overview by render(historyStrings.overview, historyIcons.overview) { list() }
    val security by render(historyStrings.security, historyIcons.security) { list() }
    val technical by render(historyStrings.technical, historyIcons.technical) { list() }
    val error by render(historyStrings.error, historyIcons.error) { list() }
    val business by render(historyStrings.business, historyIcons.business) { list() }

    override val default: Z2Builder = { list() }
}