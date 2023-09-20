package hu.simplexion.z2.browser.nonmaterial.table.builders

import hu.simplexion.z2.browser.browserStrings
import hu.simplexion.z2.commons.i18n.LocalizedText

class TableActionBuilder<T>{

    var label : LocalizedText = browserStrings._empty
    var handler: () -> Unit = { }

}