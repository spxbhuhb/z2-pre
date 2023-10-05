package hu.simplexion.z2.commons.i18n

import hu.simplexion.z2.commons.util.PublicApi

@PublicApi
interface LocalizedTextSupport : LocalizedText {
    val supportFor : String
}