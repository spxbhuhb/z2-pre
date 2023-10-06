package hu.simplexion.z2.i18n.ui

import hu.simplexion.z2.browser.routing.NavRouter
import hu.simplexion.z2.localization.ui.localizationIcons
import hu.simplexion.z2.localization.ui.localizationStrings

@Suppress("unused")
object languagesRouter : NavRouter(localizationStrings.languages, localizationIcons.languages, true, { list() })