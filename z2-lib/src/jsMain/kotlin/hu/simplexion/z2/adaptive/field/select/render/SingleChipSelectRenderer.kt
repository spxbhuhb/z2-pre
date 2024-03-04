package hu.simplexion.z2.adaptive.field.select.render

import hu.simplexion.z2.adaptive.event.Z2Event
import hu.simplexion.z2.adaptive.field.FieldRenderer
import hu.simplexion.z2.adaptive.field.select.SelectField
import hu.simplexion.z2.browser.css.*
import hu.simplexion.z2.browser.html.*
import hu.simplexion.z2.browser.material.stateLayer

open class SingleChipSelectRenderer<VT,OT>: FieldRenderer<SelectField<VT, OT>,VT> {

    override lateinit var field : SelectField<VT,OT>

    override fun render(field: SelectField<VT,OT>) {
        TODO("Not yet implemented")
    }

    fun renderItem(parent: Z2, field: SelectField<VT, OT>, value: VT) {
        with(parent) {
            addCss(
                pl12, pr12, h48,
                positionRelative, boxSizingBorderBox,
                displayGrid,
                cursorPointer
            )

            gridTemplateColumns = "1fr"

            stateLayer()

            div(alignSelfCenter) {
                renderItemValue(value)
            }

            onMouseDown {
                it.preventDefault() // to prevent focus change
            }

            onClick {
                field.fieldValue.valueOrNull = value
            }
        }
    }

    open fun Z2.renderItemValue(value: VT) {
        text { value }
    }

    override fun patch(event: Z2Event) {
        TODO("Not yet implemented")
    }

}