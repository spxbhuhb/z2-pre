package hu.simplexion.z2.adaptive.field.select.impl

import hu.simplexion.z2.adaptive.impl.adaptiveImplFactories

fun registerSelectImpl() {
    adaptiveImplFactories[DropdownListImpl.uuid] = DropdownListImpl
}