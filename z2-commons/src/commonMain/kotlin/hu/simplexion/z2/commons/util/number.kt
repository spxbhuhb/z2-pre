package hu.simplexion.z2.commons.util

import hu.simplexion.z2.localization.locales.localizedFormats
import hu.simplexion.z2.localization.text.LocalizedText
import kotlin.math.floor
import kotlin.math.log
import kotlin.math.pow

/**
 * Convert an int into a byte array (4 bytes).
 */
fun Int.toByteArray(): ByteArray = ByteArray(4).also { this.encodeInto(it) }

/**
 * Convert an int to bytes and write those bytes into [target] starting
 * from [offset].
 */
fun Int.encodeInto(target: ByteArray, offset: Int = 0) {
    for (i in 3 downTo 0) {
        target[offset + i] = (this shr (8 * (3 - i))).toByte()
    }
}

/**
 * Convert a long into a byte array (8 bytes).
 */
fun Long.toByteArray(): ByteArray = ByteArray(8).also { this.encodeInto(it) }

/**
 * Convert a long to bytes and write those bytes into [target] starting
 * from [offset].
 */
fun Long.encodeInto(target: ByteArray, offset: Int = 0) {
    for (i in 7 downTo 0) {
        target[offset + i] = (this shr (8 * (7 - i))).toByte()
    }
}

/**
 * Read a long from the byte array.
 */
fun ByteArray.toLong(offset: Int = 0): Long {
    var value: Long = 0L
    for (i in 0 until 8) {
        value = (value shl 8) or (this[offset + i].toLong() and 0xFF)
    }
    return value
}


private val sizes = listOf("", "KB", "MB", "GB", "TB", "PB", "EB", "ZB", "YB")

/**
 * Format a number into a user-friendly byte string like 2.5 GB.
 *
 * @param  emptyText  The text to use when there are zero bytes.
 *
 * @param  bytesText  The text to use when there are only a few (less than 1024) bytes.
 *                    for example
 */
@PublicApi
fun Number.formatByteLength(decimals: Int = 2, emptyText: LocalizedText, bytesText: LocalizedText): String {
    if (this.toLong() == 0L) return emptyText.toString()

    val bytes = this.toDouble()

    val k = 1024.0
    val i = floor(log(bytes, k))
    val si = bytes / k.pow(i)

    return localizedFormats.format(si, if (i < 1) 0 else decimals) + " " + if (i < 1) bytesText.toString() else sizes[i.toInt()]
}