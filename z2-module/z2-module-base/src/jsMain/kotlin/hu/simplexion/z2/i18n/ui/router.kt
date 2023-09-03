package hu.simplexion.z2.i18n.ui

import hu.simplexion.z2.browser.routing.NavRouter

@Suppress("unused")
object languagesRouter : NavRouter(i18nStrings.languages, i18nIcons.languages, true, { list() })