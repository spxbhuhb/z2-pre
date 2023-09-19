package hu.simplexion.z2.browser.components.schematic

import hu.simplexion.z2.schematic.runtime.Schematic
import hu.simplexion.z2.schematic.runtime.schema.SchemaField
import hu.simplexion.z2.schematic.runtime.schema.validation.FieldValidationResult

typealias EncodeToString<T> = (value : T) -> String

typealias DecodeFromString<T> = (value : String) -> T

typealias StringSupport<T> = Pair<EncodeToString<T>, DecodeFromString<T>>

typealias SuspendValidation<T> = suspend (value: T?) -> FieldValidationResult

typealias FullSuspendValidation<T> = suspend (schematic: Schematic<*>, field: SchemaField<T>, value: T?) -> FieldValidationResult