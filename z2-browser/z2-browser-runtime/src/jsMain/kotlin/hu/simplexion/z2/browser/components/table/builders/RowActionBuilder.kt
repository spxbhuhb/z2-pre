package hu.simplexion.z2.browser.components.table.builders

import hu.simplexion.z2.browser.browserStrings
import hu.simplexion.z2.commons.i18n.LocalizedText

class RowActionBuilder<T>{

    var label : LocalizedText = browserStrings._empty
    var handler: (row: T) -> Unit = { }

}