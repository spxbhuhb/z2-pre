package hu.simplexion.z2.adaptive.field.text.impl

import hu.simplexion.z2.adaptive.impl.adaptiveImplFactories

fun registerTextImpl() {
    adaptiveImplFactories[OutlinedTextImpl.uuid] = OutlinedTextImpl
    adaptiveImplFactories[FilledTextImpl.uuid] = FilledTextImpl
    adaptiveImplFactories[BorderBottomTextImpl.uuid] = BorderBottomTextImpl
    adaptiveImplFactories[ChipTextImpl.uuid] = ChipTextImpl
}