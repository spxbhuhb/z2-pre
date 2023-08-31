package hu.simplexion.z2.browser.components.table.builders

import hu.simplexion.z2.browser.material.basicStrings
import hu.simplexion.z2.commons.i18n.LocalizedText

class TableActionBuilder<T>{

    var label : LocalizedText = basicStrings.EMPTY
    var handler: () -> Unit = { }

}