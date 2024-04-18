package hu.simplexion.z2.adaptive.field

import hu.simplexion.z2.adaptive.field.select.impl.registerSelectImpl
import hu.simplexion.z2.adaptive.field.text.impl.registerTextImpl

fun registerFieldImpl() {
    registerSelectImpl()
    registerTextImpl()
}