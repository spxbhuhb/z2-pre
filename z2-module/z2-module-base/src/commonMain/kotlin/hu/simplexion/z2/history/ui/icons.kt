package hu.simplexion.z2.history.ui

import hu.simplexion.z2.commons.i18n.LocalizedIconStore
import hu.simplexion.z2.commons.util.UUID

@Suppress("ClassName")
internal object icons : LocalizedIconStore(UUID("e0d52bcf-a4dc-484d-9e26-ce461b6f147f")) {

    val history by "history"
    val overview by "overview"
    val security by "safety_check"
    val technical by "data_object"
    val error by "error"
    val business by "receipt"

}