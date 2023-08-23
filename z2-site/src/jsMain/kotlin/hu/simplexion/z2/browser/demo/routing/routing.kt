package hu.simplexion.z2.browser.demo.routing

import defaultLayout
import hu.simplexion.z2.browser.html.Z2
import hu.simplexion.z2.browser.html.Z2Builder
import hu.simplexion.z2.browser.html.div
import hu.simplexion.z2.browser.material.button.filledButton
import hu.simplexion.z2.browser.routing.NavRouter
import hu.simplexion.z2.browser.routing.Router
import hu.simplexion.z2.commons.util.UUID
import mainRouter

@Suppress("unused")
object routingRouter : NavRouter(strings.routing, icons.route) {

    val content by render(strings.content, icons.content) { text { "content" } }
    val subRoute by subRouter
    val paramRoute by parameterRouter
    val paramSubRoute by parameterSubRouter

    override val default: Z2Builder = {
        div {
            filledButton(strings.parameter) {
                mainRouter.openWith(paramRoute, UUID<Any>())
            }
            filledButton(strings.parameterSubRoute) {
                mainRouter.openWith(parameterSubRouter, UUID<Any>(), parameterSubRouter.paramContent.relativePath)
            }
        }
    }

}

object subRouter : Router<Z2>(strings.subRoute, icons.route) {

    val uuid by parameter()

    override fun default(receiver: Z2, path: List<String>) {
        receiver.defaultLayout(routingRouter, routingRouter.nav) { div { text { "sub route" } } }
    }
}

object parameterRouter : Router<Z2>(strings.parameter, icons.parameter) {

    val uuid by parameter()

    override fun default(receiver: Z2, path: List<String>) {
        receiver.defaultLayout(routingRouter, routingRouter.nav) { div { text { uuid } } }
    }
}

object parameterSubRouter : NavRouter(strings.parameterSubRoute, icons.parameter, routingRouter.nav) {

    val uuid by parameter()

    val paramContent by render(strings.content, icons.content) { text { uuid } }

    override val default : Z2Builder = {
        div { text { parameterRouter.uuid } }
    }
}