package hu.simplexion.z2.adaptive.field.select.impl.dropdown

import hu.simplexion.z2.adaptive.field.text.impl.BorderBottomTextImpl
import hu.simplexion.z2.adaptive.impl.AdaptiveImpl
import hu.simplexion.z2.adaptive.impl.AdaptiveImplFactory
import hu.simplexion.z2.browser.html.Z2
import hu.simplexion.z2.util.UUID

class BorderBottomDropdownListImpl<VT, OT>(parent: Z2) : AbstractDropdownListImpl<VT, OT>(parent) {

    override fun Z2.textImpl() {
        BorderBottomTextImpl(this).also { it.field = textField }.main()
    }

    companion object : AdaptiveImplFactory(UUID("065e97ef-0a5e-7e66-8000-0c9ef4247a95")) {

        override fun new(parent: AdaptiveImpl): AdaptiveImpl =
            BorderBottomDropdownListImpl<Any, Any>(parent as Z2)

    }

}