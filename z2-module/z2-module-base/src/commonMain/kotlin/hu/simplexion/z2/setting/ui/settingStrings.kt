package hu.simplexion.z2.setting.ui

import hu.simplexion.z2.commons.localization.text.LocalizedTextProvider

object settingStrings : ISettingStrings

interface ISettingStrings : LocalizedTextProvider {

    val settings get() = static("Settings")

}
