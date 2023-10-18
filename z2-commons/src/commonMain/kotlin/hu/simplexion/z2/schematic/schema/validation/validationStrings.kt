package hu.simplexion.z2.schematic.schema.validation

import hu.simplexion.z2.localization.text.LocalizedTextProvider

lateinit var validationStrings : IValidationStrings

interface IValidationStrings : LocalizedTextProvider {
    val nullFail get() = static("Value required", name = "nullFail")
    val integerFail get() = static("Integer value required", name = "integerFail")
    val minValueFail get() = static("Value is less than %N", name = "minValueFail")
    val maxValueFail get() = static("Value is greater than %N", name = "maxValueFail")
    val minLengthFail get() = static("At least %N characters required", name = "minLengthFail")
    val maxLengthFail get() = static("Maximum %N characters allowed", name = "maxLengthFail")
    val blankFail get() = static("Blank value is not allowed", name = "blankFail")
    val booleanFail get() = static("Boolean value required", name = "booleanFail")
    val patternFail get() = static("Invalid value", name = "patternFail")
    val uuidFail get() = static("UUID value required", name = "uuidFail")
    val nilFail get() = static("Non-NIL UUID value required", name = "nilFail")
    val durationFail get() = static("Duration value required", name = "durationFail")
    val instantFail get() = static("Instant value required", name = "instantFail")
    val localDateFail get() = static("Date value required", name = "localDateFail")
    val localTimeFail get() = static("Time value required", name = "localTimeFail")
    val localDateTimeFail get() = static("Date and time value required", name = "localDateTimeFail")
    val schematicFail get() = static("Value required", name = "schematicFail")
    val enumFail get() = static("Value from the pre-defined set required", name = "enumFail")
    val listFail get() = static("Value is not a list", name = "listFail")
}