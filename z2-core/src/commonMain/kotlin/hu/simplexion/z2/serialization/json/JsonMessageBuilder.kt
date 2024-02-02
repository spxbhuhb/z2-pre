package hu.simplexion.z2.serialization.json

import hu.simplexion.z2.util.UUID

/**
 * Build JSON messages.
 *
 * Use the type-specific functions to add records and then use [pack] to get
 * the wire format message.
 */
class JsonMessageBuilder {

    val writer = JsonBufferWriter()

    fun pack() = writer.pack()

    // ----------------------------------------------------------------------------
    // Boolean
    // ----------------------------------------------------------------------------

    fun boolean(fieldName: String, value: Boolean): JsonMessageBuilder {
        writer.bool(fieldName, value)
        return this
    }

    fun booleanOrNull(fieldName: String, value: Boolean?): JsonMessageBuilder {
        writer.bool(fieldName, value)
        return this
    }

    fun booleanList(fieldName: String, values: List<Boolean>): JsonMessageBuilder {
        booleanListOrNull(fieldName, values)
        return this
    }

    fun booleanListOrNull(fieldName: String, values: List<Boolean>?): JsonMessageBuilder {
        array(fieldName, values) { v, i -> writer.bool(v[i]) }
        return this
    }

    // ----------------------------------------------------------------------------
    // Int
    // ----------------------------------------------------------------------------

    fun int(fieldName: String, value: Int): JsonMessageBuilder {
        writer.number(fieldName, value)
        return this
    }

    fun intOrNull(fieldName: String, value: Int?): JsonMessageBuilder {
        writer.number(fieldName, value)
        return this
    }

    fun intList(fieldName: String, values: List<Int>): JsonMessageBuilder {
        intListOrNull(fieldName, values)
        return this
    }

    fun intListOrNull(fieldName: String, values: List<Int>?): JsonMessageBuilder {
        array(fieldName, values) { v, i -> writer.number(v[i]) }
        return this
    }

    // ----------------------------------------------------------------------------
    // Long
    // ----------------------------------------------------------------------------

    fun long(fieldName: String, value: Long): JsonMessageBuilder {
        writer.number(fieldName, value)
        return this
    }

    fun longOrNull(fieldName: String, value: Long?): JsonMessageBuilder {
        writer.number(fieldName, value)
        return this
    }

    fun longList(fieldName: String, values: List<Long>): JsonMessageBuilder {
        longListOrNull(fieldName, values)
        return this
    }

    fun longListOrNull(fieldName: String, values: List<Long>?): JsonMessageBuilder {
        array(fieldName, values) { v, i -> writer.number(v[i]) }
        return this
    }

    // ----------------------------------------------------------------------------
    // String
    // ----------------------------------------------------------------------------

    fun string(fieldName: String, value: String): JsonMessageBuilder {
        writer.string(fieldName, value)
        return this
    }

    fun stringOrNull(fieldName: String, value: String?): JsonMessageBuilder {
        writer.string(fieldName, value)
        return this
    }

    fun stringList(fieldName: String, values: List<String>): JsonMessageBuilder {
        stringListOrNull(fieldName, values)
        return this
    }

    fun stringListOrNull(fieldName: String, values: List<String>?): JsonMessageBuilder {
        array(fieldName, values) { v, i -> writer.string(v[i]) }
        return this
    }


    // ----------------------------------------------------------------------------
    // ByteArray
    // ----------------------------------------------------------------------------

    fun byteArray(fieldName: String, value: ByteArray): JsonMessageBuilder {
        writer.bytes(fieldName, value)
        return this
    }

    fun byteArrayOrNull(fieldName: String, value: ByteArray?): JsonMessageBuilder {
        writer.bytes(fieldName, value)
        return this
    }

    fun byteArrayList(fieldName: String, values: List<ByteArray>): JsonMessageBuilder {
        byteArrayListOrNull(fieldName, values)
        return this
    }

    fun byteArrayListOrNull(fieldName: String, values: List<ByteArray>?): JsonMessageBuilder {
        array(fieldName, values) { v, i -> writer.bytes(v[i]) }
        return this
    }


    // ----------------------------------------------------------------------------
    // UUID
    // ----------------------------------------------------------------------------

    /**
     * Add a UUID to the message. Uses `bytes` to store the 16 raw bytes of
     * the UUID.
     */
    fun uuid(fieldName: String, value: UUID<*>): JsonMessageBuilder {
        writer.bytes(fieldName, value.toByteArray())
        return this
    }

    fun uuidOrNull(fieldName: String, value: UUID<*>?): JsonMessageBuilder {
        writer.bytes(fieldName, value?.toByteArray())
        return this
    }

    /**
     * Add a list of UUIDs to the message. Uses packed `bytes` to store the
     * 16 raw bytes of the UUID.
     */
    fun uuidList(fieldName: String, values: List<UUID<*>>): JsonMessageBuilder {
        uuidListOrNull(fieldName, values)
        return this
    }

    fun uuidListOrNull(fieldName: String, values: List<UUID<*>>?): JsonMessageBuilder {
        array(fieldName, values) { v, i -> writer.uuid(v[i]) }
        return this
    }


    // ----------------------------------------------------------------------------
    // Non-Scalar List
    // ----------------------------------------------------------------------------

    fun <T> instanceList(fieldName: String, encoder: JsonEncoder<T>, values: List<T>): JsonMessageBuilder {
        instanceListOrNull(fieldName, encoder, values)
        return this
    }

    fun <T> instanceListOrNull(
        fieldName: String,
        encoder: JsonEncoder<T>,
        values: List<T>?
    ): JsonMessageBuilder {
        array(fieldName, values) { v, i -> encoder.encodeJson(writer, v[i]) }
        return this
    }

    // ----------------------------------------------------------------------------
    // Non-Primitive
    // ----------------------------------------------------------------------------

    fun <T> instance(fieldName: String, encoder: JsonEncoder<T>, value: T): JsonMessageBuilder {
        instanceOrNull(fieldName, encoder, value)
        return this
    }

    fun <T> instanceOrNull(
        fieldName: String,
        encoder: JsonEncoder<T>,
        value: T?
    ): JsonMessageBuilder {
        if (value == null) {
            writer.nullValue(fieldName)
        } else {
            writer.fieldName(fieldName)
            encoder.encodeJson(writer, value)
        }
        return this
    }

    // ----------------------------------------------------------------------------
    // Utility
    // ----------------------------------------------------------------------------

    fun <T> array(fieldName: String, values: List<T>?, block: (list: List<T>, index: Int) -> Unit) {
        if (values == null) {
            writer.nullValue(fieldName)
            return
        }

        writer.fieldName(fieldName)
        writer.openArray()
        val last = values.size - 1
        for (index in values.indices) {
            block(values, index)
            if (index != last) writer.separator()
        }
        writer.closeArray()
    }
}