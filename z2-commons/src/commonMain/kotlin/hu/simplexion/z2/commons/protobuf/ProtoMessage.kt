package hu.simplexion.z2.commons.protobuf

import hu.simplexion.z2.commons.util.UUID

/**
 * Parse Protocol Buffer messages.
 *
 * @param  wireFormat  The wire format message to parse. This buffer backs the parser, it should
 *                     not change until the message is in use.
 */
class ProtoMessage(
    wireFormat: ByteArray,
    offset: Int = 0,
    length: Int = wireFormat.size
) {

    val records: List<ProtoRecord> = ProtoBufferReader(wireFormat, offset, length).records()

    operator fun get(fieldNumber: Int): ProtoRecord? = records.lastOrNull { it.fieldNumber == fieldNumber }

    // -----------------------------------------------------------------------------------------
    // Boolean
    // -----------------------------------------------------------------------------------------

    fun boolean(fieldNumber: Int): Boolean = get(fieldNumber)?.let { it.value == 1UL } ?: false

    fun booleanOrNull(fieldNumber: Int, nullFieldNumber: Int): Boolean? =
        if (get(nullFieldNumber) != null) null else boolean(fieldNumber)

    fun booleanList(fieldNumber: Int) = scalarList(fieldNumber, { value.bool() }, { varint().bool() })

    fun booleanListOrNull(fieldNumber: Int, nullFieldNumber: Int): List<Boolean>? =
        if (get(nullFieldNumber) != null) null else booleanList(fieldNumber)

    // -----------------------------------------------------------------------------------------
    // Int
    // -----------------------------------------------------------------------------------------

    fun int(fieldNumber: Int): Int = get(fieldNumber)?.value?.sint32() ?: 0

    fun intOrNull(fieldNumber: Int, nullFieldNumber: Int): Int? =
        if (get(nullFieldNumber) != null) null else int(fieldNumber)

    fun intList(fieldNumber: Int) = scalarList(fieldNumber, { value.sint32() }, { varint().sint32() })

    fun intListOrNull(fieldNumber: Int, nullFieldNumber: Int): List<Int>? =
        if (get(nullFieldNumber) != null) null else intList(fieldNumber)

    // -----------------------------------------------------------------------------------------
    // Long
    // -----------------------------------------------------------------------------------------

    fun long(fieldNumber: Int): Long = get(fieldNumber)?.value?.sint64() ?: 0L

    fun longOrNull(fieldNumber: Int, nullFieldNumber: Int): Long? =
        if (get(nullFieldNumber) != null) null else long(fieldNumber)

    fun longList(fieldNumber: Int) = scalarList(fieldNumber, { value.sint64() }, { varint().sint64() })

    fun longListOrNull(fieldNumber: Int, nullFieldNumber: Int): List<Long>? =
        if (get(nullFieldNumber) != null) null else longList(fieldNumber)

    // -----------------------------------------------------------------------------------------
    // String
    // -----------------------------------------------------------------------------------------

    fun string(fieldNumber: Int): String = get(fieldNumber)?.string() ?: ""

    fun stringOrNull(fieldNumber: Int, nullFieldNumber: Int): String? =
        if (get(nullFieldNumber) != null) null else string(fieldNumber)

    fun stringList(fieldNumber: Int) = scalarList(fieldNumber, { string() }, { string() })

    fun stringListOrNull(fieldNumber: Int, nullFieldNumber: Int): List<String>? =
        if (get(nullFieldNumber) != null) null else stringList(fieldNumber)

    // -----------------------------------------------------------------------------------------
    // ByteArray
    // -----------------------------------------------------------------------------------------

    fun byteArray(fieldNumber: Int): ByteArray = get(fieldNumber)?.bytes() ?: ByteArray(0)

    fun byteArrayOrNull(fieldNumber: Int, nullFieldNumber: Int): ByteArray? =
        if (get(nullFieldNumber) != null) null else byteArray(fieldNumber)

    fun byteArrayList(fieldNumber: Int) = scalarList(fieldNumber, { bytes() }, { bytes() })

    fun byteArrayListOrNull(fieldNumber: Int, nullFieldNumber: Int): List<ByteArray>? =
        if (get(nullFieldNumber) != null) null else byteArrayList(fieldNumber)

    // -----------------------------------------------------------------------------------------
    // UUID
    // -----------------------------------------------------------------------------------------

    fun <T> uuid(fieldNumber: Int): UUID<T> = get(fieldNumber)?.uuid() ?: UUID.nil()

    fun <T> uuidOrNull(fieldNumber: Int, nullFieldNumber: Int): UUID<T>? =
        if (get(nullFieldNumber) != null) null else uuid(fieldNumber)

    fun <T> uuidList(fieldNumber: Int): List<UUID<T>> = scalarList(fieldNumber, { uuid() }, { uuid() })

    fun <T> uuidListOrNull(fieldNumber: Int, nullFieldNumber: Int): List<UUID<T>>? =
        if (get(nullFieldNumber) != null) null else uuidList(fieldNumber)

    // -----------------------------------------------------------------------------------------
    // Instance
    // -----------------------------------------------------------------------------------------

    fun <T> instance(fieldNumber: Int, decoder: ProtoDecoder<T>): T {
        val record = get(fieldNumber) ?: return decoder.decodeProto(null)
        check(record is LenProtoRecord)
        return decoder.decodeProto(record.message())
    }

    fun <T> instanceOrNull(fieldNumber: Int, nullFieldNumber: Int, decoder: ProtoDecoder<T>): T? =
        if (get(nullFieldNumber) != null) null else instance(fieldNumber, decoder)

    fun <T> instanceList(fieldNumber: Int, decoder: ProtoDecoder<T>): MutableList<T> {
        val list = mutableListOf<T>()
        for (record in records) {
            if (record.fieldNumber != fieldNumber) continue
            check(record is LenProtoRecord)
            list += decoder.decodeProto(record.message())
        }
        return list
    }

    fun <T> instanceListOrNull(fieldNumber: Int, nullFieldNumber: Int, decoder: ProtoDecoder<T>): List<T>? =
        if (get(nullFieldNumber) != null) null else instanceList(fieldNumber, decoder)

    // --------------------------------------------------------------------------------------
    // Helpers
    // --------------------------------------------------------------------------------------

    fun <T> scalarList(
        fieldNumber: Int,
        single: ProtoRecord.() -> T,
        item: ProtoBufferReader.() -> T
    ): MutableList<T> {
        val list = mutableListOf<T>()

        for (record in records) {
            if (record.fieldNumber != fieldNumber) continue

            if (record !is LenProtoRecord) {
                list += record.single()
            } else {
                list += ProtoBufferReader(record).packed(item)
            }
        }
        return list
    }

}