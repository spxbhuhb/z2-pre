package hu.simplexion.z2.browser.immaterial.select

import hu.simplexion.z2.browser.browserIcons
import hu.simplexion.z2.browser.css.*
import hu.simplexion.z2.browser.html.*
import hu.simplexion.z2.browser.material.icon.icon
import hu.simplexion.z2.browser.material.stateLayer

class SelectItem<T>(
    parent: Z2,
    var selectBase: SelectBase<T>,
    val value: T,
) : Z2(parent) {

    override fun main(): SelectItem<T> {
        update()
        return this
    }

    fun update() {
        selectBase.config.itemBuilderFun.invoke(this, selectBase, value)
    }

    companion object {
        fun <T> SelectItem<T>.defaultItemBuilderFun(selectBase: SelectBase<T>, value: T) {
            addCss(
                pl12, pr12, h48,
                positionRelative, boxSizingBorderBox,
                displayGrid,
                cursorPointer
            )
            gridTemplateColumns = if (selectBase.config.singleChipSelect) "1fr" else "36px 1fr"

            stateLayer()

            if (! selectBase.config.singleChipSelect) {
                div(w24, pr12, alignSelfCenter) {
                    if (value == selectBase.valueOrNull) {
                        icon(browserIcons.check)
                    }
                }
            }

            div(alignSelfCenter) {
                selectBase.config.itemContentFun(this, value)
            }

            onMouseDown {
                it.preventDefault() // to prevent focus change
            }

            onClick {
                selectBase.onChange(value)
                selectBase.itemSelected()
            }
        }
    }
}