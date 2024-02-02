package hu.simplexion.z2.browser.immaterial.schematic

import hu.simplexion.z2.schematic.Schematic
import hu.simplexion.z2.schematic.schema.SchemaField
import hu.simplexion.z2.schematic.schema.validation.FieldValidationResult

typealias EncodeToString<T> = (value : T) -> String

typealias DecodeFromString<T> = (value : String) -> T

typealias StringSupport<T> = Pair<EncodeToString<T>, DecodeFromString<T>>

typealias SuspendValidation<T> = suspend (value: T?) -> FieldValidationResult

typealias FullSuspendValidation<T> = suspend (schematic: Schematic<*>, field: SchemaField<T>, value: T?) -> FieldValidationResult