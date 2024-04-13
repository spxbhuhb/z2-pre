package hu.simplexion.z2.adaptive.field.select.impl

import hu.simplexion.z2.adaptive.event.EventCentral
import hu.simplexion.z2.adaptive.event.Z2Event
import hu.simplexion.z2.adaptive.field.FieldRenderer
import hu.simplexion.z2.adaptive.field.RequestBlurEvent
import hu.simplexion.z2.adaptive.field.isOf
import hu.simplexion.z2.adaptive.field.select.SelectField
import hu.simplexion.z2.adaptive.field.text.TextField
import hu.simplexion.z2.adaptive.field.text.impl.textField
import hu.simplexion.z2.adaptive.impl.AdaptiveImpl
import hu.simplexion.z2.adaptive.impl.AdaptiveImplFactory
import hu.simplexion.z2.browser.browserIcons
import hu.simplexion.z2.browser.browserStrings
import hu.simplexion.z2.browser.css.*
import hu.simplexion.z2.browser.html.*
import hu.simplexion.z2.browser.material.icon.icon
import hu.simplexion.z2.browser.material.px
import hu.simplexion.z2.browser.material.stateLayer
import hu.simplexion.z2.schematic.SchematicFieldEvent
import hu.simplexion.z2.util.UUID
import kotlinx.browser.document
import kotlinx.browser.window

class DropdownListImpl<VT, OT>(
    parent: Z2
) : Z2(parent), FieldRenderer<SelectField<VT, OT>, VT>, AdaptiveImpl {

    companion object : AdaptiveImplFactory(UUID("1de953d4-f67d-4a8a-aa0c-b9c2bcfdf4ff")) {
        override fun new(parent: AdaptiveImpl) = DropdownListImpl<Any, Any>(parent as Z2)
    }

    // java.lang.IllegalStateException: IdSignature clash:
    // hu.simplexion.z2.adaptive.impl/AdaptiveImplFactory|null[0];
    // Existed declaration
    //    CLASS IR_EXTERNAL_DECLARATION_STUB CLASS name:AdaptiveImplFactory modality:ABSTRACT visibility:public superTypes:[kotlin.Any]
    //    clashed with new
    //    CLASS IR_EXTERNAL_DECLARATION_STUB CLASS name:AdaptiveImplFactory modality:ABSTRACT visibility:public superTypes:[kotlin.Any]

    override lateinit var field: SelectField<VT, OT>

    val selectState
        get() = field.selectState

    val selectConfig
        get() = field.selectConfig

    lateinit var text: TextField

    val textValue
        get() = text.fieldValue

    var itemContainer: Z2? = null

    val items = mutableListOf<Z2>()

    override fun main(): DropdownListImpl<VT, OT> {
        text = TextField().also {
            it.fieldState = field.fieldState
            it.fieldConfig = field.fieldConfig.copy().also { it.impl = selectConfig.valueImpl }
        }

        fieldState.loading = true
        fieldConfig.trailingIcon = browserIcons.down

        textImpl()

        attach(fieldValue, fieldState, fieldConfig, selectState, selectConfig, textValue)

        if (selectConfig.eager) {
            field.runQuery("")
        } else {
            field.runGet()
        }

        return this
    }

    override fun onSchematicEvent(event: Z2Event) {

        if (hasFocus && ! readOnly) {
            showItemContainer()
        } else {
            itemContainer?.htmlElement?.remove()
            itemContainer = null
        }

        when {
            event.isOf(fieldState) -> {
                if (!hasFocus) {
                    textValue.valueOrNull =
                        if (fieldValue.valueOrNull == null) "" else selectConfig.valueToString(field, fieldValue.value)
                }
            }

            event.isOf(fieldValue) -> {
                textValue.valueOrNull = selectConfig.valueToString(field, fieldValue.value)
            }

            event.isOf(textValue) -> {
                if (selectConfig.remote) {
                    val filter = (textValue.valueOrNull ?: "")
                    if (filter.length >= selectConfig.minimumFilterLength) {
                        field.runQuery(filter)
                    }
                }
            }

            event.isOf(fieldConfig) && event is SchematicFieldEvent && event.field.name != "impl" -> {
                text.fieldConfig.schematicSet(event.field.name, fieldConfig.schematicGet(event.field.name))
            }
        }
    }

    fun Z2.textImpl() {
        textField(text)
    }

    fun itemSelected(option: OT) {
        EventCentral.fire(RequestBlurEvent(fieldState.schematicHandle))

        val value = selectConfig.optionToValue(field, option)

        fieldValue.valueOrNull = value
        fieldState.touched = true

        textValue.valueOrNull = selectConfig.valueToString(field, value)
    }

    fun showItemContainer() {

        val br = htmlElement.getBoundingClientRect()
        val wh = window.innerHeight

        val spaceAbove = br.top
        val spaceBelow = wh - br.bottom

        itemContainer?.htmlElement?.remove()

        Z2 {
            style.position = "fixed"

            addCss(
                surfaceContainer, wFull, pt8, pb8,
                borderBottomRightRadiusExtraSmall, borderBottomLeftRadiusExtraSmall,
                borderColorPrimary, borderSolid, borderWidth1,
                boxSizingBorderBox, overflowYAuto, labelLarge
            )

            itemContainer = this
            zIndex = 3000

            when {
                selectState.running -> {
                    span(pl16) { text { browserStrings.searchInProgress } }
                }

                selectState.noItems -> {
                    span(pl16) { text { browserStrings.noHits } }
                }

                selectState.options.isEmpty() && selectConfig.remote -> {
                    span(pl16) { text { browserStrings.typeMinimumCharacters } }
                }

                else -> Unit
            }

            for (option in selectState.options) {
                items += renderItem(this, option)
            }

            style.left = br.left.px
            style.minWidth = br.width.px
            style.maxWidth = "max-content"

            if (spaceBelow < 300 && spaceBelow < spaceAbove) {
                style.bottom = (wh - br.top).px
                style.maxHeight = (spaceAbove - 16).px
                addCss(borderTopLeftRadiusExtraSmall, borderTopRightRadiusExtraSmall)
            } else {
                style.top = (br.top + 56).px
                style.maxHeight = (spaceBelow - 16).px
            }
        }.also {
            itemContainer = it
            document.body !!.appendChild(it.htmlElement)
        }
    }

    fun renderItem(parent: Z2, option: OT) =
        parent.div {
            addCss(
                pl12, pr12, h48,
                positionRelative, boxSizingBorderBox,
                displayGrid,
                cursorPointer
            )

            val fieldValue = field.fieldValue.valueOrNull

            gridTemplateColumns = "36px 1fr"

            stateLayer()

            div(w24, pr12, alignSelfCenter) {
                if (option == fieldValue) {
                    icon(browserIcons.check)
                }
            }

            div(alignSelfCenter) {
                renderItemValue(option)
            }

            onMouseDown {
                it.preventDefault() // to prevent focus change
            }

            onClick {
                itemSelected(option)
            }
        }

    fun Z2.renderItemValue(option: OT) {
        text { selectConfig.optionToString(field, option) }
    }

}