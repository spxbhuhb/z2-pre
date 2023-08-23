package hu.simplexion.z2.schematic.runtime.schema.validation

import hu.simplexion.z2.commons.i18n.LocalizedText

class SchematicValidationResult(
    val valid : Boolean,
    val fieldResults : Map<String, FieldValidationResult>
)

class FieldValidationResult(
    val path : String,
    val valid : Boolean,
    val fails : List<ValidationFailInfo>
)

open class ValidationFailInfo(
    val message : String
)

fun fail(template :LocalizedText, vararg parameters : Any) : ValidationFailInfo {
    return ValidationFailInfo(
        template.toString() // TODO convert the template into a text
    )
}

object ValidationFailInfoNull : ValidationFailInfo(validationStrings.nullFail.toString())
