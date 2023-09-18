package hu.simplexion.z2.i18n.runtime

import hu.simplexion.z2.commons.i18n.locales.localizedFormats

fun Long.localizedAmount(currency: Currency) : String {
    // FIXME proper currency formatter
    return localizedFormats.format(this.toDouble(), 0)
}

