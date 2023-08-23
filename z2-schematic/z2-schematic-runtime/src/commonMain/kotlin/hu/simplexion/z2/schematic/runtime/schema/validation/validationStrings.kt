package hu.simplexion.z2.schematic.runtime.schema.validation

import hu.simplexion.z2.commons.i18n.LocalizedTextStore
import hu.simplexion.z2.commons.util.UUID

@Suppress("ClassName")
object validationStrings : LocalizedTextStore(UUID("63080e45-1b3e-41f6-aaa9-0bc6f81e12cd")) {
    val nullFail by "Value required"
    val integerFail by "Integer value required"
    val minValueFail by "Value is less than %N"
    val maxValueFail by "Value is greater than %N"
    val minLengthFail by "At least %N characters required"
    val maxLengthFail by "Maximum %N characters allowed"
    val blankFail by "Blank value is not allowed"
    val booleanFail by "Boolean value required"
    val patternFail by "Invalid value"
    val uuidFail by "UUID value required"
    val nilFail by "Non-NUL UUID value required"
    val durationFail by "Duration value required"
    val instantFail by "Instant value required"
    val localDateFail by "Date value required"
    val localDateTimeFail by "Date and time value required"
    val schematicFail by "Value required"
    val enumFail by "Value from the pre-defined set required"
}