package hu.simplexion.z2.adaptive.field.text.impl

import hu.simplexion.z2.adaptive.field.bind
import hu.simplexion.z2.adaptive.field.text.TextField
import hu.simplexion.z2.adaptive.impl.adaptiveImplFactories
import hu.simplexion.z2.browser.html.Z2
import hu.simplexion.z2.localization.label
import hu.simplexion.z2.localization.locales.localeCapitalized
import hu.simplexion.z2.schematic.access.SchematicAccessContext
import hu.simplexion.z2.schematic.schema.SchemaFieldType

fun Z2.textField(builder: TextField.() -> Unit) =
    textField(TextField { builder() })

fun Z2.textField(field: TextField) =
    (adaptiveImplFactories[field.fieldConfig.impl] !!.new(this) as AbstractTextImpl).also {
        it.field = field
        it.main()
    }

fun Z2.textField(context: SchematicAccessContext) {
    check(context.field.type == SchemaFieldType.String)
    val uiModel = TextField {
        fieldValue.valueOrNull = context.value as String?
        fieldConfig.label = context.field.label(context.schematic).localeCapitalized
    }
    val uiImpl = textField(uiModel)
    uiModel.bind(context) { uiImpl.listeners += it }
}