package hu.simplexion.z2.localization.runtime

import hu.simplexion.z2.localization.locales.localizedFormats

fun Long.localizedAmount(currency: Currency) : String {
    // FIXME proper currency formatter
    return localizedFormats.format(this.toDouble(), 0)
}

