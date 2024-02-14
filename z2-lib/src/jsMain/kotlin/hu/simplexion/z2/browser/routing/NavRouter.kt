package hu.simplexion.z2.browser.routing

import hu.simplexion.z2.auth.model.Role
import hu.simplexion.z2.browser.html.Z2
import hu.simplexion.z2.browser.html.Z2Builder
import hu.simplexion.z2.browser.layout.Content.defaultLayout
import hu.simplexion.z2.browser.material.navigation.navigationDrawer
import hu.simplexion.z2.localization.icon.LocalizedIcon
import hu.simplexion.z2.localization.text.LocalizedText
import hu.simplexion.z2.util.UUID

open class NavRouter(
    label: LocalizedText? = null,
    icon: LocalizedIcon? = null,
    loggedIn : Boolean = true,
    roles : List<UUID<Role>> = emptyList(),
    open var useParentNav: Boolean = false,
    open var default : Z2Builder = {  }
) : Router<Z2>(
    label, icon, loggedIn, roles
) {

    open var nav: Z2Builder = {
        var candidate = if (useParentNav) this@NavRouter.parent else null

        while (candidate?.parent != null && candidate is NavRouter && candidate.useParentNav) {
            candidate = candidate.parent
        }

        navigationDrawer(candidate?.targets ?: targets)
    }

    override fun render(
        label: LocalizedText?,
        icon: LocalizedIcon?,
        loggedIn: Boolean,
        roles: List<UUID<Role>>,
        renderFun: Z2Builder
    ): RoutedRenderer<Z2> {
        return super.render(label, icon, loggedIn, roles) { defaultLayout(this@NavRouter, nav, renderFun) }
    }

    override fun default(receiver: Z2, path: List<String>) {
        receiver.defaultLayout(this, nav, default)
    }

    override fun up() {
        var candidate = if (useParentNav) parent else null

        while (candidate?.parent != null && candidate is NavRouter && candidate.useParentNav) {
            candidate = candidate.parent
        }

        if (candidate == null) super.up() else candidate.up()
    }

}