package hu.simplexion.z2.browser.components.search

import hu.simplexion.z2.browser.html.Z2
import hu.simplexion.z2.commons.i18n.LocalizedText

fun <T> Z2.search(
    hint: LocalizedText
) =
    Search(
        this,
        SearchConfiguration<T>().also { it.hint = hint }
    )


infix fun <T> Search<T>.query(queryFun: suspend (filter: String) -> List<T>): Search<T> {
    configuration.queryFun = queryFun
    return this
}

infix fun <T> Search<T>.onSelect(selectFun: (value: T) -> Unit): Search<T> {
    configuration.selectFun = selectFun
    return this
}

infix fun <T> Search<T>.itemRender(itemRenderFun: Z2.(value: T) -> Unit): Search<T> {
    configuration.itemRenderFun = itemRenderFun
    return this
}

infix fun <T> Search<T>.itemText(itemTextFun: (value: T) -> String): Search<T> {
    configuration.itemTextFun = itemTextFun
    return this
}