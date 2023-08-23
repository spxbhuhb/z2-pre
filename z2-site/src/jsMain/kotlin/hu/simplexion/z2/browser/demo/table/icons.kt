package hu.simplexion.z2.browser.demo.table

import hu.simplexion.z2.commons.i18n.LocalizedIconStore
import hu.simplexion.z2.commons.util.UUID

object icons : LocalizedIconStore(UUID()) {
    val inbox by "mail"
    val outbox by "send"
    val favourites by "favorite"
    val trash by "delete"
}