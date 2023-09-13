package hu.simplexion.z2.browser.material.radiobutton

import hu.simplexion.z2.browser.field.FieldStyle
import hu.simplexion.z2.browser.html.Z2
import hu.simplexion.z2.browser.material.textfield.FieldConfig.Companion.defaultFieldStyle

class RadioGroupConfig<T>(
    val style: FieldStyle = defaultFieldStyle,
    itemBuilderFun: (Z2 .(T) -> Unit)?,
    options: List<T> = emptyList(),
    onChange : (RadioButtonGroup<T>.(value: T) -> Unit)? = null
) {

    var itemBuilderFun: Z2.(T) -> Unit = itemBuilderFun ?: { text { it } }
        set(value) {
            field = value
            update?.invoke()
        }

    var options: List<T> = options
        set(value) {
            field = value
            update?.invoke()
        }

    var onChange: (RadioButtonGroup<T>.(value: T) -> Unit)? = onChange
        set(value) {
            field = value
            update?.invoke()
        }

    var update: (() -> Unit)? = null

}