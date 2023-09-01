package hu.simplexion.z2.schematic.runtime.schema.validation

import hu.simplexion.z2.commons.i18n.LocalizedText

data class SchematicValidationResult(
    val valid : Boolean,
    val fieldResults : Map<String, FieldValidationResult>
)

data class FieldValidationResult(
    val path : String,
    val valid : Boolean,
    val fails : List<ValidationFailInfo>
)

open class ValidationFailInfo(
    val message : String
) {
    override fun toString(): String {
        return message
    }
}

fun fail(template :LocalizedText, vararg parameters : Any) : ValidationFailInfo {
    return ValidationFailInfo(
        template.toString() // TODO convert the template into a text
    )
}

object ValidationFailInfoNull : ValidationFailInfo(validationStrings.nullFail.toString())
