package hu.simplexion.z2.browser.components.schematic

import hu.simplexion.z2.schematic.runtime.Schematic
import hu.simplexion.z2.schematic.runtime.schema.SchemaField
import hu.simplexion.z2.schematic.runtime.schema.validation.FieldValidationResult

typealias SuspendValidation<T> = suspend (value: T) -> FieldValidationResult

typealias FullSuspendValidation<T> = suspend (schematic: Schematic<*>, field: SchemaField<T>, value: T) -> FieldValidationResult