package hu.simplexion.z2.browser.table.builders

import hu.simplexion.z2.browser.material.basicStrings
import hu.simplexion.z2.commons.i18n.LocalizedText

class RowActionBuilder<T>{

    var label : LocalizedText = basicStrings.EMPTY
    var handler: (row: T) -> Unit = { }

}