package hu.simplexion.z2.browser.immaterial.table.builders

import hu.simplexion.z2.browser.browserStrings
import hu.simplexion.z2.localization.text.LocalizedText

class TableActionBuilder<T>{

    var label : LocalizedText = browserStrings._empty
    var handler: () -> Unit = { }

}