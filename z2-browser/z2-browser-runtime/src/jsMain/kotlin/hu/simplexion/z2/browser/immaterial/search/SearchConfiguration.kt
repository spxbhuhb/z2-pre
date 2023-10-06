package hu.simplexion.z2.browser.immaterial.search

import hu.simplexion.z2.browser.html.Z2
import hu.simplexion.z2.commons.localization.text.LocalizedText

open class SearchConfiguration<T> {
    var minimumFilterLength = 3
    var queryFun: suspend (text: String) -> List<T> = { emptyList() }
    var hint : LocalizedText? = null
    var selectFun: (value: T) -> Unit = {  }
    var itemRenderFun : Z2.(value : T) -> Unit = { + it.toString() }
    var itemTextFun : (value : T) -> String = { it.toString() }
    var filterChangeFun : (value : String) -> Unit = {  }
}