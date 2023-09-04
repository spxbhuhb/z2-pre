/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.z2.commons.i18n.locales

import hu.simplexion.z2.commons.i18n.numbersToStringTable
import kotlinx.datetime.*


object EnLocalizedFormats : AbstractLocalizedFormats(
    LocalizationConfig(
        thousandSeparator = ",",
        decimalSeparator = "."
    )
) {
    override fun format(value: Instant): String {
        return format(value.toLocalDateTime(TimeZone.currentSystemDefault()))
    }

    override fun format(value: LocalDate): String {
        return value.toString().replace("-", "/")
    }

    override fun format(value: LocalDateTime): String {
        val year = value.year.toString()
        val month = numbersToStringTable[value.monthNumber]
        val day = numbersToStringTable[value.dayOfMonth]
        val hour = numbersToStringTable[value.hour]
        val minute = numbersToStringTable[value.minute]
        val second = numbersToStringTable[value.second]
        val micros = (value.nanosecond / 1_000).toString().padStart(6, '0')

        return "$year/$day/$month $hour:$minute:$second.$micros"
    }

    override fun capitalized(value : String) : String =
        value.split(" ").joinToString(" ") { it.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() } }

}