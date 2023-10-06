package hu.simplexion.z2.history.ui

import hu.simplexion.z2.commons.localization.icon.LocalizedIconProvider

object historyIcons : HistoryIcons

interface HistoryIcons : LocalizedIconProvider {
    val history get() = static("history")
    val overview get() = static("overview")
    val security get() = static("safety_check")
    val technical get() = static("data_object")
    val error get() = static("error")
    val business get() = static("receipt")
}