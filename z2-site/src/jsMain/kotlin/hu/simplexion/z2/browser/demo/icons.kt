package hu.simplexion.z2.browser.demo

import hu.simplexion.z2.IBaseIcons
import hu.simplexion.z2.localization.icon.LocalizedIconProvider

internal object icons : LocalizedIconProvider, IBaseIcons {
    val administration = static("local_police")
    val interfaces = static("conversion_path")
    val impressum = static("breaking_news")
    val template = static("dynamic_feed")
    val inbox = static("mail")
    val outbox = static("send")
    val favourites = static("favorite")
    val trash = static("delete")
    val content = static("content_copy")
    val route = static("route")
    val parameter = static("data_object")
}