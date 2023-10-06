package hu.simplexion.z2.schematic.runtime.schema.validation

import hu.simplexion.z2.commons.localization.text.LocalizedText

data class SchematicValidationResult(
    val valid : Boolean,
    val validForCreate : Boolean,
    val fieldResults : Map<String, FieldValidationResult>
) {
    fun failedFields(forCreate : Boolean) : List<String> =
        fieldResults.values.filter { ! if (forCreate) it.validForCreate else it.valid }.map { it.path }
}

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

fun fail(template : LocalizedText, vararg parameters : Any) : ValidationFailInfo {
    return ValidationFailInfo(
        template.toString() // TODO convert the template into a text
    )
}

object ValidationFailInfoNull : ValidationFailInfo(validationStrings.nullFail.toString())
