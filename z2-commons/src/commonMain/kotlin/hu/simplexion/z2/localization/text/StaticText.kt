package hu.simplexion.z2.localization.text

import hu.simplexion.z2.localization.localizedTextStore

class StaticText(
    override val key: String,
    override var value: String,
) : LocalizedText {

    // TODO cleanup StaticText, there are instances in the interface getters

    override fun toString(): String {
        return localizedTextStore[key]?.value ?: value
    }
}