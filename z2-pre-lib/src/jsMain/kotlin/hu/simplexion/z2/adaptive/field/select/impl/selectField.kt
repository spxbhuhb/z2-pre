package hu.simplexion.z2.adaptive.field.select.impl

import hu.simplexion.z2.adaptive.field.select.SelectField
import hu.simplexion.z2.adaptive.impl.adaptiveImplFactories
import hu.simplexion.z2.browser.html.Z2

fun <VT, OT> Z2.selectField(field: SelectField<VT, OT>) {
    @Suppress("UNCHECKED_CAST")
    (adaptiveImplFactories[field.fieldConfig.impl]!!.new(this) as DropdownListImpl<VT, OT>).also {
        it.field = field
        it.main()
    }
}