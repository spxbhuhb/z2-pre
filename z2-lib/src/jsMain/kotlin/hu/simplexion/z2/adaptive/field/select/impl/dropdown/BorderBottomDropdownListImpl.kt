package hu.simplexion.z2.adaptive.field.select.impl.dropdown

import hu.simplexion.z2.adaptive.field.select.SelectField
import hu.simplexion.z2.adaptive.field.text.impl.BorderBottomTextImpl
import hu.simplexion.z2.browser.html.Z2

class BorderBottomDropdownListImpl<VT, OT>(
    parent : Z2,
    field : SelectField<VT,OT>
) : AbstractDropdownListImpl<VT, OT>(parent, field) {

    override fun Z2.textImpl() {
        BorderBottomTextImpl(this, textField).main()
    }

}