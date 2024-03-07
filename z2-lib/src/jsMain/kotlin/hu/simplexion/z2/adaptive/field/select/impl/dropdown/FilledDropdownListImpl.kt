package hu.simplexion.z2.adaptive.field.select.impl.dropdown

import hu.simplexion.z2.adaptive.field.text.impl.FilledTextImpl
import hu.simplexion.z2.browser.html.Z2

class FilledDropdownListImpl<VT, OT>(parent: Z2) : AbstractDropdownListImpl<VT, OT>(parent) {

    override fun Z2.textImpl() {
        FilledTextImpl(this).also { it.field = textField }.main()
    }

}