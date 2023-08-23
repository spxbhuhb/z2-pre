package hu.simplexion.z2.browser.routing

import hu.simplexion.z2.commons.i18n.LocalizedIcon
import hu.simplexion.z2.commons.i18n.LocalizedText

class RoutedAction<R>(
    override val label: LocalizedText? = null,
    override val icon: LocalizedIcon? = null,
    val actionFun: () -> Unit
) : RoutingTarget<R> {

    override var parent: Router<R>? = null

    override var relativePath : String = ""

    override fun open(receiver: R, path: List<String>) {
        actionFun()
    }

}