package hu.simplexion.z2.adaptive.field.text.impl

import hu.simplexion.z2.adaptive.field.text.TextField
import hu.simplexion.z2.adaptive.impl.adaptiveImplFactories
import hu.simplexion.z2.browser.html.Z2

fun Z2.textField(field: TextField) {
    (adaptiveImplFactories[field.fieldConfig.impl]!!.new(this) as AbstractTextImpl).also {
        it.field = field
        it.main()
    }
}