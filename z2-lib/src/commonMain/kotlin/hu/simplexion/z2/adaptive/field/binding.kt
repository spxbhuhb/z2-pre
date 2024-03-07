package hu.simplexion.z2.adaptive.field

import hu.simplexion.z2.adaptive.event.EventCentral
import hu.simplexion.z2.adaptive.event.Z2Event
import hu.simplexion.z2.adaptive.event.Z2EventListener
import hu.simplexion.z2.schematic.Schematic
import hu.simplexion.z2.schematic.SchematicFieldEvent
import hu.simplexion.z2.schematic.SchematicValidation
import hu.simplexion.z2.schematic.access.SchematicAccessContext
import hu.simplexion.z2.schematic.schema.SchemaField
import hu.simplexion.z2.schematic.schema.validation.SchematicValidationResult
import hu.simplexion.z2.util.Z2Handle
import hu.simplexion.z2.util.nextHandle

fun <VT> AdaptiveField<VT>.bind(context: SchematicAccessContext, registerFun : (Z2EventListener) -> Unit) {
    // FIXME adaptive and schematic binding does not handle invalid field value

    val l1 = SchematicToAdaptiveFieldListener(
        context.schematic,
        context.field,
        fieldValue,
        fieldState
    )

    EventCentral.attach(l1)
    registerFun(l1)

    val l2 = AdaptiveFieldToSchematicListener(
        fieldValue,
        context.schematic,
        context.field
    )

    EventCentral.attach(l2)
    registerFun(l2)
}

private class SchematicToAdaptiveFieldListener(
    val schematic : Schematic<*>,
    val schematicField : SchemaField<*>,
    val adaptiveValue : FieldValue<*>,
    val adaptiveState : FieldState
) : Z2EventListener {

    override val busHandle: Z2Handle = schematic.schematicHandle

    override val listenerHandle: Z2Handle = nextHandle()

    override fun accept(event: Z2Event) {
        when (event) {
            is SchematicFieldEvent -> fieldEvent(event)
            is SchematicValidation -> validation(event.validationResult)
        }
    }

    private fun fieldEvent(event: SchematicFieldEvent) {
        // this has to come first as validation result may change even if the value remains the same
        validation(event.validationResult)

        if (event.field != schematicField) return

        val currentAdaptiveValue = adaptiveValue.valueOrNull
        val currentSchematicValue = schematic.schematicGet(schematicField.name)

        if (currentAdaptiveValue != currentSchematicValue) {
            adaptiveValue.schematicSet("valueOrNull", currentSchematicValue)
        }
    }

    private fun validation(validationResult: SchematicValidationResult) {
        val fieldResult = validationResult.fieldResults[schematicField.name] ?: return

        if (fieldResult.valid) {
            adaptiveState.error = false
            adaptiveState.errorText = null
        } else {
            adaptiveState.error = true
            adaptiveState.errorText = fieldResult.fails.firstOrNull()?.message
        }
    }

}

private class AdaptiveFieldToSchematicListener(
    val adaptiveValue : FieldValue<*>,
    val schematic : Schematic<*>,
    val schematicField : SchemaField<*>,
) : Z2EventListener {

    override val busHandle: Z2Handle = adaptiveValue.schematicHandle

    override val listenerHandle: Z2Handle = nextHandle()

    override fun accept(event: Z2Event) {
        when (event) {
            is SchematicFieldEvent -> fieldEvent()
        }
    }

    private fun fieldEvent() {
        val currentAdaptiveValue = adaptiveValue.valueOrNull
        val currentSchematicValue = schematic.schematicGet(schematicField.name)

        if (currentSchematicValue != currentAdaptiveValue) {
            schematic.schematicSet(schematicField.name, currentSchematicValue)
        }
    }
}
