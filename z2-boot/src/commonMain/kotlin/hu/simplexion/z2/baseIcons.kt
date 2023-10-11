package hu.simplexion.z2

import hu.simplexion.z2.localization.icon.LocalizedIconProvider

object baseIcons : IBaseIcons

interface IBaseIcons : LocalizedIconProvider {
    val roles get() = static("recent_actors")
    val accounts get() = static("manage_accounts")
    val securityPolicy get() = static("security")
    val folder get() = static("folder")
    val history get() = static("history")
    val overview get() = static("overview")
    val security get() = static("safety_check")
    val technical get() = static("data_object")
    val error get() = static("error")
    val business get() = static("receipt")
    val languages get() = static("translate")
    val worker get() = static("forward_circle")
}