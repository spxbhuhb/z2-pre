/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.z2.i18n.locales

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
        val month = numbersToString_0_60[value.monthNumber]
        val day = numbersToString_0_60[value.dayOfMonth]
        val hour = numbersToString_0_60[value.hour]
        val minute = numbersToString_0_60[value.minute]
        val second = numbersToString_0_60[value.second]
        val micros = (value.nanosecond / 1_000).toString().padStart(6, '0')

        return "$year/$day/$month $hour:$minute:$second.$micros"
    }
}