package hu.simplexion.z2.commons.protobuf

import hu.simplexion.z2.commons.util.UUID

/**
 * Build Protocol Buffer messages.
 *
 * Use the type-specific functions to add records and then use [pack] to get
 * the wire format message.
 */
class ProtoMessageBuilder {

    val writer = ProtoBufferWriter()

    fun pack() = writer.pack()

    // ----------------------------------------------------------------------------
    // Boolean
    // ----------------------------------------------------------------------------

    fun boolean(fieldNumber: Int, value: Boolean): ProtoMessageBuilder {
        writer.bool(fieldNumber, value)
        return this
    }

    fun booleanOrNull(fieldNumber: Int, nullFieldNumber: Int, value: Boolean?): ProtoMessageBuilder {
        if (value == null) {
            writer.bool(nullFieldNumber, true)
        } else {
            writer.bool(fieldNumber, value)
        }
        return this
    }

    fun booleanList(fieldNumber: Int, values: List<Boolean>): ProtoMessageBuilder {
        sub(fieldNumber) {
            for (value in values) {
                it.bool(value)
            }
        }
        return this
    }

    fun booleanListOrNull(fieldNumber: Int, nullFieldNumber: Int, values: List<Boolean>?): ProtoMessageBuilder {
        if (values == null) {
            writer.bool(nullFieldNumber, true)
        } else {
            booleanList(fieldNumber, values)
        }
        return this
    }

    // ----------------------------------------------------------------------------
    // Int
    // ----------------------------------------------------------------------------

    fun int(fieldNumber: Int, value: Int): ProtoMessageBuilder {
        writer.sint32(fieldNumber, value)
        return this
    }

    fun intOrNull(fieldNumber: Int, nullFieldNumber: Int, value: Int?): ProtoMessageBuilder {
        if (value == null) {
            writer.bool(nullFieldNumber, true)
        } else {
            writer.sint32(fieldNumber, value)
        }
        return this
    }

    fun intList(fieldNumber: Int, values: List<Int>): ProtoMessageBuilder {
        sub(fieldNumber) {
            for (value in values) {
                it.sint32(value)
            }
        }
        return this
    }

    fun intListOrNull(fieldNumber: Int, nullFieldNumber: Int, values: List<Int>?): ProtoMessageBuilder {
        if (values == null) {
            writer.bool(nullFieldNumber, true)
        } else {
            intList(fieldNumber, values)
        }
        return this
    }

    // ----------------------------------------------------------------------------
    // Long
    // ----------------------------------------------------------------------------

    fun long(fieldNumber: Int, value: Long): ProtoMessageBuilder {
        writer.sint64(fieldNumber, value)
        return this
    }

    fun longOrNull(fieldNumber: Int, nullFieldNumber: Int, value: Long?): ProtoMessageBuilder {
        if (value == null) {
            writer.bool(nullFieldNumber, true)
        } else {
            writer.sint64(fieldNumber, value)
        }
        return this
    }

    fun longList(fieldNumber: Int, values: List<Long>): ProtoMessageBuilder {
        sub(fieldNumber) {
            for (value in values) {
                it.sint64(value)
            }
        }
        return this
    }

    fun longListOrNull(fieldNumber: Int, nullFieldNumber: Int, values: List<Long>?): ProtoMessageBuilder {
        if (values == null) {
            writer.bool(nullFieldNumber, true)
        } else {
            longList(fieldNumber, values)
        }
        return this
    }

    // ----------------------------------------------------------------------------
    // String
    // ----------------------------------------------------------------------------

    fun string(fieldNumber: Int, value: String): ProtoMessageBuilder {
        writer.string(fieldNumber, value)
        return this
    }

    fun stringOrNull(fieldNumber: Int, nullFieldNumber: Int, value: String?): ProtoMessageBuilder {
        if (value == null) {
            writer.bool(nullFieldNumber, true)
        } else {
            writer.string(fieldNumber, value)
        }
        return this
    }

    fun stringList(fieldNumber: Int, values: List<String>): ProtoMessageBuilder {
        sub(fieldNumber) {
            for (value in values) {
                it.string(value)
            }
        }
        return this
    }

    fun stringListOrNull(fieldNumber: Int, nullFieldNumber: Int, values: List<String>?): ProtoMessageBuilder {
        if (values == null) {
            writer.bool(nullFieldNumber, true)
        } else {
            stringList(fieldNumber, values)
        }
        return this
    }


    // ----------------------------------------------------------------------------
    // ByteArray
    // ----------------------------------------------------------------------------

    fun byteArray(fieldNumber: Int, value: ByteArray): ProtoMessageBuilder {
        writer.bytes(fieldNumber, value)
        return this
    }

    fun byteArrayOrNull(fieldNumber: Int, nullFieldNumber: Int, value: ByteArray?): ProtoMessageBuilder {
        if (value == null) {
            writer.bool(nullFieldNumber, true)
        } else {
            writer.bytes(fieldNumber, value)
        }
        return this
    }

    fun byteArrayList(fieldNumber: Int, values: List<ByteArray>): ProtoMessageBuilder {
        sub(fieldNumber) {
            for (value in values) {
                it.bytes(value)
            }
        }
        return this
    }

    fun byteArrayListOrNull(fieldNumber: Int, nullFieldNumber: Int, values: List<ByteArray>?): ProtoMessageBuilder {
        if (values == null) {
            writer.bool(nullFieldNumber, true)
        } else {
            byteArrayList(fieldNumber, values)
        }
        return this
    }


    // ----------------------------------------------------------------------------
    // UUID
    // ----------------------------------------------------------------------------

    /**
     * Add a UUID to the message. Uses `bytes` to store the 16 raw bytes of
     * the UUID.
     */
    fun uuid(fieldNumber: Int, value: UUID<*>): ProtoMessageBuilder {
        writer.bytes(fieldNumber, value.toByteArray())
        return this
    }

    fun uuidOrNull(fieldNumber: Int, nullFieldNumber: Int, value: UUID<*>?): ProtoMessageBuilder {
        if (value == null) {
            writer.bool(nullFieldNumber, true)
        } else {
            writer.bytes(fieldNumber, value.toByteArray())
        }
        return this
    }

    /**
     * Add a list of UUIDs to the message. Uses packed `bytes` to store the
     * 16 raw bytes of the UUID.
     */
    fun uuidList(fieldNumber: Int, values: List<UUID<*>>): ProtoMessageBuilder {
        sub(fieldNumber) {
            for (value in values) {
                it.bytes(value.toByteArray())
            }
        }
        return this
    }

    fun uuidListOrNull(fieldNumber: Int, nullFieldNumber: Int, values: List<UUID<*>>?): ProtoMessageBuilder {
        if (values == null) {
            writer.bool(nullFieldNumber, true)
        } else {
            uuidList(fieldNumber, values)
        }
        return this
    }


    // ----------------------------------------------------------------------------
    // Non-Scalar List
    // ----------------------------------------------------------------------------

    fun <T> instanceList(fieldNumber: Int, encoder: ProtoEncoder<T>, values: List<T>): ProtoMessageBuilder {
        for (value in values) {
            writer.bytes(fieldNumber, encoder.encodeProto(value))
        }
        return this
    }

    fun <T> instanceListOrNull(
        fieldNumber: Int,
        nullFieldNumber: Int,
        encoder: ProtoEncoder<T>,
        values: List<T>?
    ): ProtoMessageBuilder {
        if (values == null) {
            writer.bool(nullFieldNumber, true)
        } else {
            for (value in values) {
                writer.bytes(fieldNumber, encoder.encodeProto(value))
            }
        }
        return this
    }

    // ----------------------------------------------------------------------------
    // Non-Primitive
    // ----------------------------------------------------------------------------

    fun <T> instance(fieldNumber: Int, encoder: ProtoEncoder<T>, value: T): ProtoMessageBuilder {
        writer.bytes(fieldNumber, encoder.encodeProto(value))
        return this
    }

    fun <T> instanceOrNull(
        fieldNumber: Int,
        nullFieldNumber: Int,
        encoder: ProtoEncoder<T>,
        value: T?
    ): ProtoMessageBuilder {
        if (value == null) {
            writer.bool(nullFieldNumber, true)
        } else {
            writer.bytes(fieldNumber, encoder.encodeProto(value))
        }
        return this
    }

    // ----------------------------------------------------------------------------
    // Utility
    // ----------------------------------------------------------------------------

    fun sub(fieldNumber: Int, block: (sub: ProtoBufferWriter) -> Unit) {
        val sub = ProtoBufferWriter()
        block(sub)
        writer.bytes(fieldNumber, sub.pack())
    }
}