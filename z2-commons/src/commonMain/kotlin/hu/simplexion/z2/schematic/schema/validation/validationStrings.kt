package hu.simplexion.z2.schematic.schema.validation

import hu.simplexion.z2.localization.text.LocalizedTextProvider

object validationStrings : IValidationStrings

interface IValidationStrings : LocalizedTextProvider {
    val nullFail get() = static("Value required")
    val integerFail get() = static("Integer value required")
    val minValueFail get() = static("Value is less than %N")
    val maxValueFail get() = static("Value is greater than %N")
    val minLengthFail get() = static("At least %N characters required")
    val maxLengthFail get() = static("Maximum %N characters allowed")
    val blankFail get() = static("Blank value is not allowed")
    val booleanFail get() = static("Boolean value required")
    val patternFail get() = static("Invalid value")
    val uuidFail get() = static("UUID value required")
    val nilFail get() = static("Non-NUL UUID value required")
    val durationFail get() = static("Duration value required")
    val instantFail get() = static("Instant value required")
    val localDateFail get() = static("Date value required")
    val localTimeFail get() = static("Time value required")
    val localDateTimeFail get() = static("Date and time value required")
    val schematicFail get() = static("Value required")
    val enumFail get() = static("Value from the pre-defined set required")
    val listFail get() = static("Value is not a list")
}