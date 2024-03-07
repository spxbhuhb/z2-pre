package hu.simplexion.z2.adaptive.field.select.impl

import hu.simplexion.z2.adaptive.field.select.impl.dropdown.BorderBottomDropdownListImpl
import hu.simplexion.z2.adaptive.impl.adaptiveImplFactories

fun registerSelectImpl() {
    adaptiveImplFactories[BorderBottomDropdownListImpl.uuid] = BorderBottomDropdownListImpl
}