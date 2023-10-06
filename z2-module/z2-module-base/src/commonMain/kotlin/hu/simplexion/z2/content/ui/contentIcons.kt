package hu.simplexion.z2.content.ui

import hu.simplexion.z2.commons.localization.icon.LocalizedIconProvider

object contentIcons : ContentIcons

interface ContentIcons : LocalizedIconProvider {
    val folder get() = static("folder")
}