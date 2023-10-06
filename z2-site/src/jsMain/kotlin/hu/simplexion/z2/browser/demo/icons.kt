package hu.simplexion.z2.browser.demo

import hu.simplexion.z2.commons.localization.icon.LocalizedIconProvider
import hu.simplexion.z2.localization.ui.ILocalizationIcons

internal object icons : LocalizedIconProvider, ILocalizationIcons {
    val administration = static("local_police")
    val accounts = static("manage_accounts")
    val roles = static("recent_actors")
    val interfaces = static("conversion_path")
    val securityPolicy = static("security")
    val impressum = static("breaking_news")
    val history = static("history")
    val template = static("dynamic_feed")
    val inbox = static("mail")
    val outbox = static("send")
    val favourites = static("favorite")
    val trash = static("delete")
    val content = static("content_copy")
    val route = static("route")
    val parameter = static("data_object")
}