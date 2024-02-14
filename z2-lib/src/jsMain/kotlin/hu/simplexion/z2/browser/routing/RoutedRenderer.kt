package hu.simplexion.z2.browser.routing

import hu.simplexion.z2.auth.model.Role
import hu.simplexion.z2.browser.html.Z2
import hu.simplexion.z2.localization.icon.LocalizedIcon
import hu.simplexion.z2.localization.text.LocalizedText
import hu.simplexion.z2.util.UUID

class RoutedRenderer<R>(
    override val label: LocalizedText? = null,
    override val icon: LocalizedIcon? = null,
    override val loggedIn: Boolean = false,
    override val roles: List<UUID<Role>> = emptyList(),
    val renderFun: R.() -> Unit
) : RoutingTarget<R> {

    override var parent: Router<R>? = null

    override var relativePath : String = ""

    override fun open(receiver: R, path: List<String>) {
        if (receiver is Z2) receiver.clear() // TODO think about RouterRenderer receiver clear
        receiver.renderFun()
    }

}