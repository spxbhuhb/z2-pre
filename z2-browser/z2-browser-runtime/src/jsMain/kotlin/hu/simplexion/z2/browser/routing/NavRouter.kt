package hu.simplexion.z2.browser.routing

import hu.simplexion.z2.browser.html.Z2
import hu.simplexion.z2.browser.html.Z2Builder
import hu.simplexion.z2.browser.layout.Content.defaultLayout
import hu.simplexion.z2.browser.material.navigation.navigationDrawer
import hu.simplexion.z2.commons.i18n.LocalizedIcon
import hu.simplexion.z2.commons.i18n.LocalizedText

open class NavRouter(
    label: LocalizedText? = null,
    icon: LocalizedIcon? = null,
    nav : Z2Builder? = null
) : Router<Z2>(
    label, icon
) {

    open val nav: Z2Builder = nav ?: { navigationDrawer(if (useParentNav) this@NavRouter.parent?.targets ?: emptyList() else targets) }

    open var useParentNav : Boolean = false

    open val default: Z2Builder = { }

    override fun render(label: LocalizedText?, icon: LocalizedIcon?, renderFun: Z2Builder): RoutedRenderer<Z2> {
        return super.render(label, icon) { defaultLayout(this@NavRouter, nav, renderFun) }
    }

    override fun default(receiver: Z2, path: List<String>) {
        receiver.defaultLayout(this, nav, default)
    }

}