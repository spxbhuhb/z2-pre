package hu.simplexion.z2.browser.nonmaterial.select

import hu.simplexion.z2.browser.css.*
import hu.simplexion.z2.browser.field.FieldState
import hu.simplexion.z2.browser.html.Z2
import hu.simplexion.z2.browser.material.px
import hu.simplexion.z2.browser.material.textfield.AbstractField
import kotlinx.browser.document
import kotlinx.browser.window

class SelectBase<T>(
    parent: Z2,
    state: FieldState,
    override val config: SelectConfig<T>
) : AbstractField<T>(parent, state, config) {

    val items = mutableListOf<SelectItem<T>>()

    lateinit var search: Z2
    var itemContainer: Z2? = null

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
        super.main()

        return this
    }

    override fun dispose() {
        super.dispose()
        itemContainer?.htmlElement?.remove()
    }

    override fun update() {
        super.update()

        if (hasFocus) {
            showItemContainer()
        } else {
            itemContainer?.htmlElement?.remove()
            itemContainer = null
        }
    }

    fun itemSelected() {
        inputElement.blur()
    }

    fun showItemContainer() {

        val br = htmlElement.getBoundingClientRect()
        val wh = window.innerHeight

        val spaceAbove = br.top
        val spaceBelow = wh - br.bottom

        itemContainer?.htmlElement?.remove()

        Z2 {
            style.position = "fixed"

            addClass(
                surfaceContainer, widthFull, pt8, pb8,
                borderBottomRightRadiusExtraSmall, borderBottomLeftRadiusExtraSmall,
                borderColorPrimary, borderSolid, borderWidth1,
                boxSizingBorderBox, overflowYAuto
            )

            itemContainer = this
            zIndex = 3000

            for (option in config.options) {
                items += SelectItem(this, this@SelectBase, option).main()
            }

            style.left = br.left.px
            style.width = br.width.px

            if (spaceBelow < 300 && spaceBelow < spaceAbove) {
                style.bottom = (wh - br.top).px
                style.maxHeight = (spaceAbove - 16).px
                addClass(borderTopLeftRadiusExtraSmall, borderTopRightRadiusExtraSmall)
            } else {
                style.top = (br.top + 56).px
                style.maxHeight = spaceBelow.px
            }

        }.also {
            itemContainer = it
            document.body !!.appendChild(it.htmlElement)
        }
    }
}