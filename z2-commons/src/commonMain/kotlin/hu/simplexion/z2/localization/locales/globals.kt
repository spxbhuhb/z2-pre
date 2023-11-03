/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.z2.localization.locales

import hu.simplexion.z2.localization.localized
import hu.simplexion.z2.localization.text.LocalizedText
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime

fun setLocalizedFormats(locale: String) {
    localizedFormats = when (locale.lowercase()) {
        "hu" -> HuLocalizedFormats
        "en" -> EnLocalizedFormats
        else -> EnLocalizedFormats
    }
}

/**
 * Stores an instance that is able to format and parse different data types
 * according to the current locale.
 */
var localizedFormats: LocalizedFormats = EnLocalizedFormats

// ---- Boolean ----

inline val Boolean.localized: String
    get() = localizedFormats.format(this)

// ---- Int ----

inline val Int.localized: String
    get() = localizedFormats.format(this)

// ---- Long ----

inline val Long.localized: String
    get() = localizedFormats.format(this)

fun Long.toDecimalString(scale : Int) : String =
    localizedFormats.format(this.toDouble() / AbstractLocalizedFormats.shifts[scale], scale)

// ---- Double ----

inline val Double.localized: String
    get() = localizedFormats.format(this)

// ---- Instant ----

inline val Instant.localized: String
    get() = localizedFormats.format(this)

inline val String.toInstant: Instant
    get() = localizedFormats.toInstant(this)

inline val String.toInstantOrNull: Instant?
    get() = localizedFormats.toInstantOrNull(this)

// ---- LocalDate ----

inline val LocalDate.localized: String
    get() = localizedFormats.format(this)

inline val String.toLocalDate: LocalDate
    get() = localizedFormats.toLocalDate(this)

inline val String.toLocalDateOrNull: LocalDate?
    get() = localizedFormats.toLocalDateOrNull(this)

// ---- LocalDateTime ----

inline val LocalDateTime.localized: String
    get() = localizedFormats.format(this)

inline val String.toLocalDateTime: LocalDateTime
    get() = localizedFormats.toLocalDateTime(this)

inline val String.toLocalDateTimeOrNull: LocalDateTime?
    get() = localizedFormats.toLocalDateTimeOrNull(this)

// ---- String ----

inline val String.localeCapitalized
    get() = localizedFormats.capitalized(this)

inline val LocalizedText.localeCapitalized
    get() = localizedFormats.capitalized(this.toString())

inline val Enum<*>.localeCapitalized
    get() = localizedFormats.capitalized(this.localized)