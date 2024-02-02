package hu.simplexion.z2.browser.material.popup

import hu.simplexion.z2.browser.html.Z2

fun Z2.popup(
    minHeight: Double = 100.0,
    minWidth: Double = 100.0,
    builder : Z2.() -> Unit
) = PopupBase(this, minHeight, minWidth, builder)
