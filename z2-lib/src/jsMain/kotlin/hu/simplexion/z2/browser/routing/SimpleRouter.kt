package hu.simplexion.z2.browser.routing

import hu.simplexion.z2.browser.html.Z2
import hu.simplexion.z2.browser.html.Z2Builder
import hu.simplexion.z2.localization.icon.LocalizedIcon
import hu.simplexion.z2.localization.text.LocalizedText

class SimpleRouter(
    label: LocalizedText? = null,
    icon: LocalizedIcon? = null,
    loggedIn : Boolean = true,
    roles : List<String> = emptyList(),
    val builder : Z2Builder
) : Router<Z2>(label, icon, loggedIn, roles) {
    override fun default(receiver: Z2, path: List<String>) {
        receiver.builder()
    }
}
