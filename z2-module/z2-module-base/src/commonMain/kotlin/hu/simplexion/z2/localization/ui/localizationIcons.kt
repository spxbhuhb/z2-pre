package hu.simplexion.z2.localization.ui

import hu.simplexion.z2.commons.localization.icon.LocalizedIconProvider

object localizationIcons : ILocalizationIcons

interface ILocalizationIcons : LocalizedIconProvider {
    val languages get() = static("translate")
}