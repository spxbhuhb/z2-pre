package hu.simplexion.z2.util

import kotlin.io.encoding.Base64.Default.encode
import kotlin.io.encoding.ExperimentalEncodingApi
import kotlin.math.min

fun String.toCamelCaseWords() : String {
    val out = mutableListOf<Char>()
    for (char in toCharArray()) {
        when {
            out.isEmpty() -> out += char.uppercaseChar()
            char.isUpperCase() -> {
                out += ' '
                out += char
            }
            else -> out += char
        }
    }
    return out.toCharArray().concatToString()
}

fun String.toCamelCase(lowerCaseFirstChar: Boolean = false): String {
    val out = mutableListOf<Char>()
    for (char in toCharArray()) {
        when {
            out.isEmpty() -> out += if (lowerCaseFirstChar) char.lowercaseChar() else char.uppercaseChar()
            char.isUpperCase() -> out += char
            else -> out += char
        }
    }
    return out.toCharArray().concatToString()
}

/**
 * Convert a [ByteArray] into a string, replacing all non-printable and non-ASCII
 * characters with a dot.
 *
 * @param  limit  Maximum number of characters in the return value. Might be less
 *                if the array is smaller.
 */
fun ByteArray.toDotString(limit: Int = this.size, offset: Int = 0): String {

    val end = kotlin.math.min(offset + limit, this.size)

    val chars = CharArray(end - offset)
    var charIndex = 0

    for (i in offset until end) {
        val byte = this[i].toInt()
        if (byte < 0x20 || byte > 0x7f) {
            chars[charIndex ++] = '.'
        } else {
            chars[charIndex ++] = Char(byte)
        }
    }

    return chars.concatToString()
}

/**
 * Check if `this` version is after the `other` version. Check [compareToVersion] for
 * comparison details.
 *
 * @return  true if `this` version is after [other], false if `this` equals [other] or `this` is before [other]
 *
 * @throws  NumberFormatException  on invalid version formats
 */
fun String?.afterVersion(other: String?): Boolean {
    return this.compareToVersion(other) > 0
}

/**
 * Check if `this` version is before the `other` version. Check [compareToVersion] for
 * comparison details.
 *
 * @return  true if `this` version is before [other], false if `this` equals [other] or `this` is after [other]
 *
 * @throws  NumberFormatException  on invalid version formats
 */
fun String?.beforeVersion(other: String?): Boolean {
    return this.compareToVersion(other) < 0
}

/**
 * Check if `this` version is the same as the `other` version. Check [compareToVersion] for
 * comparison details. This is not the same as [String.equals] as leading zeroes are skipped.
 *
 * @return  true if `this` equals [other], false otherwise
 *
 * @throws  NumberFormatException  on invalid version formats
 */
fun String?.equalsVersion(other: String?): Boolean {
    return this.compareToVersion(other) == 0
}

/**
 * Compares this string to [other] as version numbers. The strings should be in
 * `<i1>.<i2>. ... <iN>-<qualifier>` format where `iN` is an integer and `qualifier`
 * is a string without dots. The parts are converted to integers and then compared.
 * Two versions that differ only in the qualifier the one without is the larger.
 *
 * @return  0 if the two versions are equal, -1 if [other] is after `this`, 1 if
 *          [other] is before this
 *
 * @throws  NumberFormatException  on invalid version formats
 */
fun String?.compareToVersion(other: String?): Int {
    if (other == null && this == null) return 0
    if (other == null) return 1
    if (this == null) return - 1

    val thisParts = this.split(".")
    val otherParts = other.split(".")

    val commonSize = min(thisParts.size, otherParts.size)

    for (i in 0 until commonSize) {
        val thisPart = thisParts[i]
        val otherPart = otherParts[i]
        if (thisPart == otherPart) continue

        val thisPartQualifier = thisPart.substringAfter('-', missingDelimiterValue = "").lowercase()
        val otherPartQualifier = otherPart.substringAfter('-', missingDelimiterValue = "").lowercase()

        val thisPartInt = thisPart.substringBefore('-').toInt()
        val otherPartInt = otherPart.substringBefore('-').toInt()

        if (thisPartInt == otherPartInt && thisPartQualifier == otherPartQualifier) continue

        return when {
            thisPartInt == otherPartInt -> {
                when {
                    thisPartQualifier.isNotEmpty() -> {
                        require(otherPartQualifier.isEmpty()) { "different qualifiers, cannot compare versions" }
                        return - 1
                    }

                    otherPartQualifier.isNotEmpty() -> {
                        require(thisPartQualifier.isEmpty()) { "different qualifiers, cannot compare versions" }
                        return 1
                    }

                    else -> return 0
                }
            }

            thisPartInt > otherPartInt -> 1
            thisPartInt < otherPartInt -> - 1
            '-' in thisPart -> - 1
            else -> 1
        }
    }

    return when {
        thisParts.size > otherParts.size -> 1
        thisParts.size < otherParts.size -> - 1
        else -> 0
    }
}

@OptIn(ExperimentalEncodingApi::class)
fun randomBase64String256Bit(): String {
    val key = ByteArray(32)
    val random = fourRandomInt() + fourRandomInt()
    for (i in random.indices) {
        random[i].encodeInto(key, i * 4)
    }
    return encode(key)
}

fun String.toSnakeCase(): String {
    val out = mutableListOf<Char>()
    var inNumber = false
    for (char in toCharArray()) {

        when {
            char.isUpperCase() -> {
                out += '-'
                out += char.lowercaseChar()
                inNumber = false
            }

            char.isDigit() -> {
                if (!inNumber) out += '-'
                out += char
                inNumber = true
            }

            else -> {
                out += char
                inNumber = false
            }
        }
    }
    return out.toCharArray().concatToString()
}