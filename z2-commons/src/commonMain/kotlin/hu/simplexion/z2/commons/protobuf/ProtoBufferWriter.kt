package hu.simplexion.z2.commons.protobuf

import hu.simplexion.z2.commons.util.UUID
import kotlin.experimental.or
import kotlin.math.max

/**
 * A low level protobuf writer to write information into a list of ByteArrays
 * in protobuf format. The list grows as more space needed up until the maximum
 * size set in the `maximumBufferSize` property of the companion object.
 *
 * @property  initialBufferSize     Size of the first buffer.
 * @property  additionalBufferSize  Size of the buffers that are added on-demand.
 * @property  maximumBufferSize     Maximum total size of buffers. The writer throws an exception if
 *                                  this is not enough.
 */
@Suppress("SpellCheckingInspection")
class ProtoBufferWriter(
    val initialBufferSize: Int = 200,
    val additionalBufferSize: Int = 10_000,
    val maximumBufferSize: Int = 5_000_000 + initialBufferSize
) {

    // ------------------------------------------------------------------------
    // Public interface
    // ------------------------------------------------------------------------

    /**
     * Number of bytes written.
     */
    val size
        get() = pastBufferByteCount + writeOffset

    /**
     * Pack all the buffers into one.
     */
    fun pack(): ByteArray {
        val data = ByteArray(size)
        var offset = 0
        val last = buffers.last()
        for (buffer in buffers) {
            if (buffer !== last) {
                buffer.copyInto(data, offset)
            } else {
                buffer.copyInto(data, offset, 0, writeOffset)
            }
            offset += buffer.size
        }
        return data
    }

    fun bool(fieldNumber: Int, value: Boolean) {
        tag(fieldNumber, VARINT)
        if (value) varint(1UL) else varint(0UL)
    }

    fun int32(fieldNumber: Int, value: Int) {
        tag(fieldNumber, VARINT)
        varint(value.toULong())
    }

    fun sint32(fieldNumber: Int, value: Int) {
        tag(fieldNumber, VARINT)
        varint(((value shl 1) xor (value shr 31)).toULong())
    }

    fun sint32(value: Int) {
        varint(((value shl 1) xor (value shr 31)).toULong())
    }

    fun uint32(fieldNumber: Int, value: UInt) {
        tag(fieldNumber, VARINT)
        varint(value.toULong())
    }

    fun fixed32(fieldNumber: Int, value: UInt) {
        tag(fieldNumber, I32)
        i32(value.toULong())
    }

    fun sfixed32(fieldNumber: Int, value: Int) {
        tag(fieldNumber, I32)
        i32(value.toULong())
    }

    fun int64(fieldNumber: Int, value: Long) {
        tag(fieldNumber, VARINT)
        varint(value.toULong())
    }

    fun sint64(fieldNumber: Int, value: Long) {
        tag(fieldNumber, VARINT)
        varint(((value shl 1) xor (value shr 63)).toULong())
    }

    fun sint64(value: Long) {
        varint(((value shl 1) xor (value shr 63)).toULong())
    }

    fun uint64(fieldNumber: Int, value: ULong) {
        tag(fieldNumber, VARINT)
        varint(value)
    }

    fun fixed64(fieldNumber: Int, value: ULong) {
        tag(fieldNumber, I64)
        i64(value)
    }

    fun sfixed64(fieldNumber: Int, value: Long) {
        tag(fieldNumber, I64)
        i64(value.toULong())
    }

    fun float(fieldNumber: Int, value: Float) {
        tag(fieldNumber, I32)
        i32(value.toRawBits().toULong())
    }

    fun double(fieldNumber: Int, value: Double) {
        tag(fieldNumber, I64)
        i64(value.toRawBits().toULong())
    }

    fun string(fieldNumber: Int, value: String) {
        tag(fieldNumber, LEN)
        put(value.encodeToByteArray())
    }

    fun uuid(fieldNumber : Int, value : UUID<*>) {
        tag(fieldNumber, LEN)
        string(value.toString())
    }

    fun bytes(fieldNumber: Int, value: ByteArray) {
        tag(fieldNumber, LEN)
        put(value)
    }

    // ------------------------------------------------------------------------
    // Functions without tag number, these are dangerous
    // ------------------------------------------------------------------------

    internal fun bool(value: Boolean) {
        if (value) varint(1UL) else varint(0UL)
    }

    internal fun int32(value: Int) {
        varint(value.toULong())
    }

    internal fun string(value: String) {
        put(value.encodeToByteArray())
    }

    internal fun uuid(value : UUID<*>) {
        string(value.toString())
    }

    internal fun bytes(value: ByteArray) {
        put(value)
    }

    // ------------------------------------------------------------------------
    // Wire format writers
    // ------------------------------------------------------------------------

    private fun tag(fieldNumber: Int, type: Int) {
        val tag = (fieldNumber.toULong() shl 3) or type.toULong()
        varint(tag)
    }

    private fun i32(value: ULong): ULong {
        var remaining = value
        for (i in 0 until 4) {
            put((remaining and 0xffUL).toByte())
            remaining = remaining shr 8
        }
        return value
    }

    private fun i64(value: ULong): ULong {
        var remaining = value
        for (i in 0 until 8) {
            put((remaining and 0xffUL).toByte())
            remaining = remaining shr 8
        }
        return value
    }

    private fun varint(value: ULong) {
        var next = value and valueMask
        var remaining = value shr 7

        while (remaining != 0UL) {
            put(continuation or next.toByte())
            next = remaining and valueMask
            remaining = remaining shr 7
        }

        put(next.toByte())
    }

    // ------------------------------------------------------------------------
    // Buffer management
    // ------------------------------------------------------------------------

    /**
     * Sum of valuable byte counts in all buffers but the last.
     */
    private var pastBufferByteCount = 0

    /**
     * Write offset in the current buffer.
     */
    private var writeOffset = 0

    private val buffers = mutableListOf(ByteArray(initialBufferSize))

    private var buffer = buffers.last()

    private fun put(byte: Byte) {
        if (writeOffset == buffer.size) {
            addBuffer()
        }

        buffer[writeOffset++] = byte
    }

    /**
     * If [byteArray] fits into the current buffer, copy it into the current
     * buffer.
     *
     * If [byteArray] does not fit into the current buffer, fill all the available
     * part and then allocate a new buffer. The new buffer will be big enough (if
     * [maximumBufferSize] allows) to hold all the remaining data.
     */
    private fun put(byteArray: ByteArray) {
        varint(byteArray.size.toULong())

        val availableSpace = buffer.size - writeOffset
        var copyOffset = 0
        val length = byteArray.size

        if (length > availableSpace) {
            byteArray.copyInto(buffer, writeOffset, 0, availableSpace)
            copyOffset = availableSpace
            addBuffer(max(additionalBufferSize, length - availableSpace))
        }

        byteArray.copyInto(buffer, writeOffset, copyOffset, length)
        writeOffset += length - copyOffset
    }

    /**
     * Add a new buffer to the list of buffers and set it as the current buffer.
     */
    private fun addBuffer(requestedSize: Int = additionalBufferSize) {
        pastBufferByteCount += buffer.size

        check(pastBufferByteCount + requestedSize < maximumBufferSize) { "ProtoBufferWriter buffer overflow" }

        buffer = ByteArray(requestedSize)
        buffers.add(buffer)
        writeOffset = 0
    }

    // ------------------------------------------------------------------------
    // Constants for the wire format
    // ------------------------------------------------------------------------

    companion object {
        const val continuation = 0x80.toByte()
        const val valueMask = 0x7fUL
    }

}