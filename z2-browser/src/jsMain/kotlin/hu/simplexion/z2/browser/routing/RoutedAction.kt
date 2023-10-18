package hu.simplexion.z2.browser.routing

import hu.simplexion.z2.localization.icon.LocalizedIcon
import hu.simplexion.z2.localization.text.LocalizedText

class RoutedAction<R>(
    override val label: LocalizedText? = null,
    override val icon: LocalizedIcon? = null,
    override val loggedIn: Boolean = false,
    override val roles: List<String> = emptyList(),
    val actionFun: () -> Unit
) : RoutingTarget<R> {

    override var parent: Router<R>? = null

    override var relativePath : String = ""

    override fun open(receiver: R, path: List<String>) {
        actionFun()
    }

}