package hu.simplexion.z2.browser

import hu.simplexion.z2.commons.localization.icon.LocalizedIconProvider

object browserIcons : BrowserIcons

interface BrowserIcons : LocalizedIconProvider {
    val settings get() = static("settings")
    val search get() = static("search")
    val cancel get() = static("cancel")
    val error get() = static("error")
    val more get() = static("more_vert")
    val close get() = static("close")
    val switchSelected get() = static("check")
    val switchUnselected get() = static("close")
    val filter get() = static("tune")
    val add get() = static("add")
    val export get() = static("download")
    val back get() = static("arrow_back")
    val administration get() = static("local_police")
    val left get() = static("chevron_left")
    val right get() = static("chevron_right")
    val down get() = static("arrow_drop_down")
    val radioButtonChecked get() = static("radio_button_checked")
    val radioButtonUnchecked get() = static("radio_button_unchecked")
    val check get() = static("check")
    val calendar get() = static("calendar_today")
    val edit get() = static("edit")
    val schedule get() = static("schedule")
}