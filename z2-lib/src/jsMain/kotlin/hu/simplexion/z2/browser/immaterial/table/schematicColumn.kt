package hu.simplexion.z2.browser.immaterial.table

import hu.simplexion.z2.browser.browserIcons
import hu.simplexion.z2.browser.css.justifyContentFlexEnd
import hu.simplexion.z2.browser.css.pr12
import hu.simplexion.z2.browser.immaterial.table.builders.ColumnBuilder
import hu.simplexion.z2.browser.immaterial.table.builders.TableBuilder
import hu.simplexion.z2.browser.material.em
import hu.simplexion.z2.browser.material.icon.icon
import hu.simplexion.z2.localization.label
import hu.simplexion.z2.localization.locales.localized
import hu.simplexion.z2.localization.locales.toDecimalString
import hu.simplexion.z2.localization.localized
import hu.simplexion.z2.schematic.Schematic
import hu.simplexion.z2.schematic.SchematicAccessFunction
import hu.simplexion.z2.schematic.access.SchematicAccessContext
import hu.simplexion.z2.schematic.schema.SchemaField
import hu.simplexion.z2.schematic.schema.SchemaFieldType
import hu.simplexion.z2.schematic.schema.field.*
import hu.simplexion.z2.schematic.schema.field.stereotype.EmailSchemaField
import hu.simplexion.z2.schematic.schema.field.stereotype.NullableEmailSchemaField
import hu.simplexion.z2.schematic.schema.field.stereotype.NullablePhoneNumberSchemaField
import hu.simplexion.z2.schematic.schema.field.stereotype.PhoneNumberSchemaField
import kotlinx.datetime.Instant

/**
 * An input for the schematic field accessed by [accessor]. The actual input depends on
 * the type of the field.
 */
@SchematicAccessFunction
fun <T : Schematic<T>> TableBuilder<T>.schematicColumn(
    context: SchematicAccessContext? = null,
    @Suppress("UNUSED_PARAMETER") accessor: () -> Any?
): ColumnBuilder<T> {
    checkNotNull(context)

    val field = context.field
    val schematicLabel = field.label(context.schematic)

    val col = when (field.type) {

        SchemaFieldType.Boolean ->
            column {
                val filterText = schematicLabel.value.lowercase()
                label = schematicLabel
                render = { schematic -> if (field.booleanValue(schematic) == true) icon(browserIcons.check) }
                comparator = { a, b -> compare(field.booleanValue(a), field.booleanValue(b)) }
                filter = { schematic, filter -> field.booleanValue(schematic) == true && filter in filterText }
            }

        SchemaFieldType.Decimal ->
            column {
                field as DecimalSchemaFieldDefault
                label = schematicLabel
                render = { schematic ->
                    addCss(justifyContentFlexEnd, pr12)
                    text { field.decimalValue(schematic)?.toDecimalString(field.scale) }
                }
                comparator = { a, b -> compare(field.decimalValue(a), field.decimalValue(b)) }
                filter =
                    { schematic, filter -> field.decimalValue(schematic)?.toDecimalString(field.scale).matches(filter) }
                initialSize = 10.em
            }

        SchemaFieldType.Duration ->
            column {
                label = schematicLabel
                render = { schematic -> text { field.durationValue(schematic) } }
                comparator = { a, b -> compare(field.durationValue(a), field.durationValue(b)) }
                filter = { schematic, filter -> field.durationValue(schematic).matches(filter) }
            }

        SchemaFieldType.Email ->
            column {
                label = schematicLabel
                render = { schematic -> text { field.emailValue(schematic) } }
                comparator = { a, b -> compare(field.emailValue(a), field.emailValue(b)) }
                filter = { schematic, filter -> field.emailValue(schematic).matches(filter) }

            }

        SchemaFieldType.Enum ->
            column {
                label = schematicLabel
                render = { schematic -> text { field.enumValue(schematic)?.localized } }
                comparator = { a, b -> compare(field.enumValue(a)?.localized, field.enumValue(b)?.localized) }
                filter = { schematic, filter -> field.enumValue(schematic)?.localized.matches(filter) }
            }

        SchemaFieldType.Instant ->
            column {
                label = schematicLabel
                render = { schematic ->
                    field.instantValue(schematic).also {
                        if (it != Instant.DISTANT_PAST && it != Instant.DISTANT_FUTURE) text { it?.localized }
                    }
                }
                comparator = { a, b -> compare(field.instantValue(a), field.instantValue(b)) }
                filter =
                    { schematic, filter -> field.instantValue(schematic)?.localized.matches(filter) }
            }

        SchemaFieldType.Int ->
            column {
                label = schematicLabel
                render = { schematic ->
                    addCss(justifyContentFlexEnd, pr12)
                    text { field.intValue(schematic)?.localized }
                }
                comparator = { a, b -> compare(field.intValue(a), field.intValue(b)) }
                filter = { schematic, filter -> field.intValue(schematic)?.localized.matches(filter) }
                initialSize = 10.em
            }

        SchemaFieldType.LocalDate ->
            column {
                label = schematicLabel
                render = { schematic -> text { field.localDateValue(schematic)?.localized } }
                comparator = { a, b -> compare(field.localDateValue(a), field.localDateValue(b)) }
                filter =
                    { schematic, filter -> field.localDateValue(schematic)?.localized.matches(filter) }
                initialSize = 7.em
            }

        SchemaFieldType.LocalDateTime ->
            column {
                label = schematicLabel
                render = { schematic -> text { field.localDateTimeValue(schematic)?.localized } }
                comparator = { a, b -> compare(field.localDateTimeValue(a), field.localDateTimeValue(b)) }
                filter =
                    { schematic, filter -> field.localDateValue(schematic)?.localized.matches(filter) }
            }

        SchemaFieldType.LocalTime ->
            column {
                label = schematicLabel
                render = { schematic -> text { field.localTimeValue(schematic) } }
                comparator = { a, b -> compare(field.localTimeValue(a), field.localTimeValue(b)) }
                filter =
                    { schematic, filter -> field.localTimeValue(schematic).matches(filter) }
            }

        SchemaFieldType.Long ->
            column {
                label = schematicLabel
                render = { schematic ->
                    addCss(justifyContentFlexEnd, pr12)
                    text { field.longValue(schematic)?.localized }
                }
                comparator = { a, b -> compare(field.longValue(a), field.longValue(b)) }
                filter = { schematic, filter -> field.longValue(schematic)?.localized.matches(filter) }
                initialSize = 10.em
            }

        SchemaFieldType.PhoneNumber ->
            column {
                label = schematicLabel
                render = { schematic -> text { field.phoneValue(schematic) } }
                comparator = { a, b -> compare(field.phoneValue(a), field.phoneValue(b)) }
                filter = { schematic, filter -> field.phoneValue(schematic).matches(filter) }
            }

        SchemaFieldType.Secret ->
            column {
                label = schematicLabel
                render = { _ -> text { "********" } }
                comparator = { _, _ -> 0 }
            }

        SchemaFieldType.String ->
            column {
                label = schematicLabel
                render = { schematic -> text { field.stringValue(schematic) } }
                comparator = { a, b -> compare(field.stringValue(a), field.stringValue(b)) }
                filter = { schematic, filter -> field.stringValue(schematic).matches(filter) }
            }

        SchemaFieldType.UUID ->
            column {
                label = schematicLabel
                render = { schematic -> text { field.uuidValue(schematic) } }
                comparator = { a, b -> compare(field.uuidValue(a), field.uuidValue(b)) }
                filter = { schematic, filter -> field.uuidValue(schematic).matches(filter) }
            }

        else -> throw NotImplementedError("schema field type ${field.type} not implemented in schematicColumn")
    }

    col.exportFun = { field.getValue(it) }
    col.exportHeader = col.label
    col.fieldType = field

    return col
}

private fun <T : Comparable<T>> compare(a: T?, b: T?): Int {
    if (a == b) return 0
    if (a == null) return -1
    if (b == null) return 1
    return a.compareTo(b)
}

private fun SchemaField<*>.booleanValue(schematic: Schematic<*>) =
    if (this.isNullable) {
        (this as NullableBooleanSchemaField).getValue(schematic)
    } else {
        (this as BooleanSchemaField).getValue(schematic)
    }

private fun SchemaField<*>.decimalValue(schematic: Schematic<*>) =
    if (this.isNullable) {
        (this as NullableDecimalSchemaField).getValue(schematic)
    } else {
        (this as DecimalSchemaField).getValue(schematic)
    }

private fun SchemaField<*>.durationValue(schematic: Schematic<*>) =
    if (this.isNullable) {
        (this as NullableDurationSchemaField).getValue(schematic)
    } else {
        (this as DurationSchemaField).getValue(schematic)
    }

private fun SchemaField<*>.emailValue(schematic: Schematic<*>) =
    if (this.isNullable) {
        (this as NullableEmailSchemaField).getValue(schematic)
    } else {
        (this as EmailSchemaField).getValue(schematic)
    }


private fun SchemaField<*>.enumValue(schematic: Schematic<*>) =
    if (this.isNullable) {
        (this as NullableEnumSchemaField<*>).getValue(schematic)
    } else {
        (this as EnumSchemaField<*>).getValue(schematic)
    }

private fun SchemaField<*>.instantValue(schematic: Schematic<*>) =
    if (this.isNullable) {
        (this as NullableInstantSchemaField).getValue(schematic)
    } else {
        (this as InstantSchemaField).getValue(schematic)
    }

private fun SchemaField<*>.intValue(schematic: Schematic<*>) =
    if (this.isNullable) {
        (this as NullableIntSchemaField).getValue(schematic)
    } else {
        (this as IntSchemaField).getValue(schematic)
    }

private fun SchemaField<*>.localDateValue(schematic: Schematic<*>) =
    if (this.isNullable) {
        (this as NullableLocalDateSchemaField).getValue(schematic)
    } else {
        (this as LocalDateSchemaField).getValue(schematic)
    }

private fun SchemaField<*>.localDateTimeValue(schematic: Schematic<*>) =
    if (this.isNullable) {
        (this as NullableLocalDateTimeSchemaField).getValue(schematic)
    } else {
        (this as LocalDateTimeSchemaField).getValue(schematic)
    }

private fun SchemaField<*>.localTimeValue(schematic: Schematic<*>) =
    if (this.isNullable) {
        (this as NullableLocalTimeSchemaField).getValue(schematic)
    } else {
        (this as LocalTimeSchemaField).getValue(schematic)
    }

private fun SchemaField<*>.longValue(schematic: Schematic<*>) =
    if (this.isNullable) {
        (this as NullableLongSchemaField).getValue(schematic)
    } else {
        (this as LongSchemaField).getValue(schematic)
    }

private fun SchemaField<*>.phoneValue(schematic: Schematic<*>) =
    if (this.isNullable) {
        (this as NullablePhoneNumberSchemaField).getValue(schematic)
    } else {
        (this as PhoneNumberSchemaField).getValue(schematic)
    }

private fun SchemaField<*>.stringValue(schematic: Schematic<*>) =
    if (this.isNullable) {
        (this as NullableStringSchemaField).getValue(schematic)
    } else {
        (this as StringSchemaField).getValue(schematic)
    }

@Suppress("UNCHECKED_CAST") // we don't care about the exact type here
private fun SchemaField<*>.uuidValue(schematic: Schematic<*>) =
    if (this.isNullable) {
        (this as NullableUuidSchemaField<Any>).getValue(schematic)
    } else {
        (this as UuidSchemaField<Any>).getValue(schematic)
    }

private fun String?.matches(filterText: String) = this?.lowercase()?.let { filterText in it } ?: false

private fun Any?.matches(filterText: String) = this?.toString()?.lowercase()?.let { filterText in it } ?: false