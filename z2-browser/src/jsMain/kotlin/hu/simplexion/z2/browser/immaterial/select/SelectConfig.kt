package hu.simplexion.z2.browser.immaterial.select

import hu.simplexion.z2.browser.field.FieldStyle
import hu.simplexion.z2.browser.html.Z2
import hu.simplexion.z2.browser.immaterial.select.SelectItem.Companion.defaultItemBuilderFun
import hu.simplexion.z2.browser.material.textfield.AbstractField
import hu.simplexion.z2.browser.material.textfield.FieldConfig

class SelectConfig<T>(
    style: FieldStyle = defaultFieldStyle,
    options: List<T> = emptyList(),
    onSelectedFun: ((field: AbstractField<T>) -> Unit)? = null,
    itemBuilderFun: SelectItemBuilderFun<T>? = null
) : FieldConfig<T>(style, { throw IllegalStateException() }, onSelectedFun) {

    /**
     * The text representation of the item. Called by [itemContentFun].
     * Default uses [toString].
     */
    var itemTextFun : (value : T) -> String = { it.toString() }

    /**
     * Builds the content part of the item, surrounded by the styling from [defaultItemBuilderFun].
     * Default adds the text generated by [itemTextFun].
     */
    var itemContentFun : Z2.(value : T) -> Unit = { + itemTextFun(it) }

    /**
     * Builds the whole item, default calls [itemContentFun].
     */
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

    var singleChipSelect : Boolean = false
        set(value) {
            field = value
            update?.invoke()
        }
}