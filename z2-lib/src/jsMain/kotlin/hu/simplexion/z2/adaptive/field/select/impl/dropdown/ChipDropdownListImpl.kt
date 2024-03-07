package hu.simplexion.z2.adaptive.field.select.impl.dropdown

import hu.simplexion.z2.adaptive.field.text.impl.ChipTextImpl
import hu.simplexion.z2.browser.html.Z2

class ChipDropdownListImpl<VT, OT>(parent: Z2) : AbstractDropdownListImpl<VT, OT>(parent) {

    override fun Z2.textImpl() {
        ChipTextImpl(this).also { it.field = textField }.main()
    }

}