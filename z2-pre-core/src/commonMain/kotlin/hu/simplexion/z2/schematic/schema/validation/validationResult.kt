package hu.simplexion.z2.schematic.schema.validation

import hu.simplexion.z2.localization.text.LocalizedText

data class SchematicValidationResult(
    val valid : Boolean,
    val validForCreate : Boolean,
    val fieldResults : Map<String, FieldValidationResult>
)

data class FieldValidationResult(
    val path : String,
    val valid : Boolean,
    val validForCreate : Boolean,
    val fails : List<ValidationFailInfo>
)

open class ValidationFailInfo(
    val message : String
) {
    override fun toString(): String {
        return message
    }
}

@Suppress("UNUSED_PARAMETER")
fun fail(template : LocalizedText, vararg parameters : Any) : ValidationFailInfo {
    return ValidationFailInfo(
        template.toString() // TODO convert the template into a text
    )
}