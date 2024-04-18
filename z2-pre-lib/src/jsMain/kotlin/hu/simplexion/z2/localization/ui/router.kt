package hu.simplexion.z2.localization.ui

import hu.simplexion.z2.baseIcons
import hu.simplexion.z2.baseStrings
import hu.simplexion.z2.browser.routing.NavRouter

val LanguageList = NavRouter(baseStrings.languages, baseIcons.languages, useParentNav = true) { list() }