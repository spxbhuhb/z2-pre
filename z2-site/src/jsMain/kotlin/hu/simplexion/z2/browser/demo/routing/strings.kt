package hu.simplexion.z2.browser.demo.routing

import hu.simplexion.z2.commons.i18n.LocalizedTextStore
import hu.simplexion.z2.commons.util.UUID

@Suppress("unused")
internal object strings : LocalizedTextStore(UUID("c44188ac-8ae0-4fe3-8aad-1bca38c3418b")) {
    val routing by "Routing"
    val content by "Content"
    val subRoute by "Sub-Route"
    val parameter by "Parameter"
    val parameterSubRoute by "Parameter Sub-Route"
}