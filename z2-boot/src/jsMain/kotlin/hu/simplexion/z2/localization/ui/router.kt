package hu.simplexion.z2.localization.ui

import hu.simplexion.z2.baseIcons
import hu.simplexion.z2.baseStrings
import hu.simplexion.z2.browser.routing.NavRouter

@Suppress("unused")
object languagesRouter : NavRouter(baseStrings.languages, baseIcons.languages, true, { list() })