/*
 * Copyright © 2020-2023, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.z2.localization.locales

import kotlinx.datetime.*
import kotlin.math.*

abstract class AbstractLocalizedFormats(
    val config: LocalizationConfig
) : LocalizedFormats {

    companion object {
        const val maxDecimals = 10
        val shifts = Array(maxDecimals) { idx -> 10.0.pow(idx) }
    }

    // -------------------------------------------------------------------------
    // Boolean
    // -------------------------------------------------------------------------

    override fun format(value: Boolean): String {
        return value.toString()
    }

    override fun toBoolean(value: String): Boolean {
        return value.toBooleanStrict()
    }

    override fun toBooleanOrNull(value: String): Boolean? {
        return value.toBooleanStrictOrNull()
    }

    // -------------------------------------------------------------------------
    // Int
    // -------------------------------------------------------------------------

    override fun format(value: Int): String {
        return value.toString()
    }

    override fun toInt(value: String): Int {
        return value.toInt()
    }

    override fun toIntOrNull(value: String): Int? {
        return value.toIntOrNull()
    }

    // -------------------------------------------------------------------------
    // Long
    // -------------------------------------------------------------------------

    override fun format(value: Long): String {
        return if (value < 0) {
            "-" + abs(value).toString().withSeparators(config.thousandSeparator)
        } else {
            value.toString().withSeparators(config.thousandSeparator)
        }
    }

    override fun toLong(value: String): Long {
        return value.replace(config.thousandSeparator, "").toLong()
    }

    override fun toLongOrNull(value: String): Long? {
        return value.replace(config.thousandSeparator, "").toLongOrNull()
    }

    // -------------------------------------------------------------------------
    // Double
    // -------------------------------------------------------------------------

    override fun format(value: Double): String {
        return value.toString()
    }

    // TODO replace this with JavaScripts 'Number.toLocaleString'
    override fun format(value: Double, decimals: Int): String {
        check(decimals < maxDecimals) { "decimals must to be less than $maxDecimals" }

        return when {
            value.isNaN() -> "NaN"
            value.isInfinite() -> if (value < 0) "-Inf" else "+Inf"
            decimals == 0 -> {
                round(value).toString().substringBefore('.').withSeparators(config.thousandSeparator).let {
                    if (it == "-0") "0" else it
                }
            }

            else -> {
                val integral = if (value < 0.0) {
                    ceil(value)
                } else {
                    floor(value)
                }

                val decimal = abs(round((value - integral) * shifts[decimals])).toLong().toString().padStart(decimals, '0')

                return integral.toString().substringBefore('.').withSeparators(" ") + config.decimalSeparator + decimal
            }
        }
    }

    override fun toDouble(value: String): Double {
        return toDoubleOrNull(value) ?: throw NumberFormatException("cannot interpret $value as a number")
    }

    override fun toDoubleOrNull(value: String): Double? {
        val p = value.split(config.decimalSeparator)

        if (p.size > 2) throw NumberFormatException(value)

        val integralPart = if (p[0].isEmpty()) {
            0L
        } else {
            p[0].split(config.thousandSeparator).forEachIndexed { index, part ->
                if (index != 0 && part.length != 3) throw NumberFormatException()
            }

            p[0].replace(config.thousandSeparator, "").toLongOrNull() ?: throw NumberFormatException()
        }

        val decimalPart = if (p.size == 1 || p[1].isEmpty()) {
            0
        } else {
            p[1].toLongOrNull() ?: throw NumberFormatException()
        }

        return integralPart.toDouble() + (if (decimalPart == 0L) 0.0 else decimalPart / shifts[p[1].length])
    }


    // -------------------------------------------------------------------------
    // Instant
    // -------------------------------------------------------------------------

    override fun format(value: Instant): String {
        val local = value.toLocalDateTime(TimeZone.currentSystemDefault()).toString()
        return local.substring(0, 10) + " " + local.substring(11, 19)
    }

    override fun toInstant(value: String): Instant {
        return Instant.parse(value)
    }

    override fun toInstantOrNull(value: String): Instant? {
        return try {
            toInstant(value)
        } catch (ex: Exception) {
            null
        }
    }

    // -------------------------------------------------------------------------
    // LocalDate
    // -------------------------------------------------------------------------

    override fun format(value: LocalDate): String {
        return value.toString()
    }

    override fun toLocalDate(value: String): LocalDate {
        return LocalDate.parse(value)
    }

    override fun toLocalDateOrNull(value: String): LocalDate? {
        return try {
            toLocalDate(value)
        } catch (ex: Exception) {
            null
        }
    }


    // -------------------------------------------------------------------------
    // LocalDateTime
    // -------------------------------------------------------------------------

    override fun format(value: LocalDateTime): String {
        return value.toString().replace('T', ' ')
    }

    override fun toLocalDateTime(value: String): LocalDateTime {
        return LocalDateTime.parse(value)
    }

    override fun toLocalDateTimeOrNull(value: String): LocalDateTime? {
        val fixedValue = value.replace(' ', 'T')
        return try {
            toLocalDateTime(fixedValue)
        } catch (ex: Exception) {
            null
        }
    }

    // -------------------------------------------------------------------------
    // Utility
    // -------------------------------------------------------------------------

    fun String.withSeparators(separator: String) =
        reversed()
            .chunked(3)
            .joinToString(separator)
            .reversed()
}