package hu.simplexion.z2.browser.nonmaterial.select

import hu.simplexion.z2.browser.field.FieldStyle
import hu.simplexion.z2.browser.material.textfield.AbstractField
import hu.simplexion.z2.browser.material.textfield.FieldConfig
import hu.simplexion.z2.browser.nonmaterial.select.SelectItem.Companion.defaultItemBuilderFun

class SelectConfig<T>(
    style: FieldStyle = defaultFieldStyle,
    options: List<T> = emptyList(),
    onSelectedFun: ((field: AbstractField<T>) -> Unit)? = null,
    itemBuilderFun: SelectItemBuilderFun<T>? = null
) : FieldConfig<T>(style, { throw IllegalStateException() }, onSelectedFun) {

    var itemTextFun : (value : T) -> String = { it.toString() }

    var itemBuilderFun: SelectItemBuilderFun<T> = itemBuilderFun ?: { sb, value -> defaultItemBuilderFun(sb, value) }
        set(value) {
            field = value
            update?.invoke()
        }

    var options: List<T> = options
        set(value) {
            field = value
            update?.invoke()
        }

}