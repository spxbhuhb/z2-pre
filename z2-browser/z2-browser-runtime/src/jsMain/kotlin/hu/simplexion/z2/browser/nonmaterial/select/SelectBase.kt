package hu.simplexion.z2.browser.nonmaterial.select

import hu.simplexion.z2.browser.css.*
import hu.simplexion.z2.browser.field.FieldState
import hu.simplexion.z2.browser.html.Z2
import hu.simplexion.z2.browser.html.div
import hu.simplexion.z2.browser.material.px
import hu.simplexion.z2.browser.material.textfield.AbstractField

class SelectBase<T>(
    parent: Z2,
    state: FieldState,
    override val config: SelectConfig<T>
) : AbstractField<T>(parent, state, config) {

    val items = mutableListOf<SelectItem<T>>()

    lateinit var search: Z2
    lateinit var itemContainer: Z2

    override var value: T
        get() = checkNotNull(valueOrNull)
        set(value) {
            valueOrNull = value
        }

    override var valueOrNull: T? = null
        set(value) {
            field = value
            inputElement.value = if (value != null) config.itemTextFun(value) else ""
            update()
        }

    fun onChange(entry: T) {
        valueOrNull = entry
        config.onChange?.invoke(this)
    }

    override fun main(): SelectBase<T> {
        addClass(positionRelative)

        div(
            positionAbsolute, surfaceContainer, widthFull, pt8, pb8,
            borderBottomRightRadiusExtraSmall, borderBottomLeftRadiusExtraSmall,
            borderColorPrimary, borderSolid, borderWidth1,
            boxSizingBorderBox
        )
        {
            itemContainer = this
            style.top = 56.px
            zIndex = 100
        }

        super.main()

        return this
    }

    override fun update() {
        super.update()

        if (hasFocus) {
            updateOptions()
            itemContainer.removeClass(displayNone)
        } else {
            itemContainer.addClass(displayNone)
        }
    }

    fun updateOptions() {
        // TODO optimize SelectBase.updateOptions, no need for redrawing during each update
        itemContainer.apply {
            clear()
            items.clear()
            for (option in config.options) {
                items += SelectItem(this, this@SelectBase, option).main()
            }
        }
    }

    fun itemSelected() {
        inputElement.blur()
    }

}