package hu.simplexion.z2.serialization.protobuf

import hu.simplexion.z2.util.UUID
import kotlin.enums.EnumEntries


object ProtoOneUnit : ProtoDecoder<Unit> {
    override fun decodeProto(message: ProtoMessage?) {}
}

// ---------------------------------------------------------------------------
// Boolean
// ---------------------------------------------------------------------------

object ProtoOneBoolean : ProtoDecoder<Boolean> {
    override fun decodeProto(message: ProtoMessage?): Boolean =
        message?.boolean(1) ?: false
}

object ProtoOneBooleanOrNull : ProtoDecoder<Boolean?> {
    override fun decodeProto(message: ProtoMessage?): Boolean? =
        message?.booleanOrNull(1, 2)
}

object ProtoOneBooleanList : ProtoDecoder<List<Boolean>> {
    override fun decodeProto(message: ProtoMessage?): List<Boolean> =
        message?.booleanList(1) ?: emptyList()
}

object ProtoOneBooleanListOrNull : ProtoDecoder<List<Boolean>?> {
    override fun decodeProto(message: ProtoMessage?): List<Boolean>? =
        message?.booleanListOrNull(1, 2)
}

// ---------------------------------------------------------------------------
// Int
// ---------------------------------------------------------------------------

object ProtoOneInt : ProtoDecoder<Int> {
    override fun decodeProto(message: ProtoMessage?): Int =
        message?.int(1) ?: 0
}

object ProtoOneIntOrNull : ProtoDecoder<Int?> {
    override fun decodeProto(message: ProtoMessage?): Int? =
        message?.intOrNull(1, 2)
}

object ProtoOneIntList : ProtoDecoder<List<Int>> {
    override fun decodeProto(message: ProtoMessage?): List<Int> =
        message?.intList(1) ?: emptyList()
}

object ProtoOneIntListOrNull : ProtoDecoder<List<Int>?> {
    override fun decodeProto(message: ProtoMessage?): List<Int>? =
        message?.intListOrNull(1, 2)
}

// ---------------------------------------------------------------------------
// Long
// ---------------------------------------------------------------------------

object ProtoOneLong : ProtoDecoder<Long> {
    override fun decodeProto(message: ProtoMessage?): Long =
        message?.long(1) ?: 0L
}

object ProtoOneLongOrNull : ProtoDecoder<Long?> {
    override fun decodeProto(message: ProtoMessage?): Long? =
        message?.longOrNull(1, 2)
}

object ProtoOneLongList : ProtoDecoder<List<Long>> {
    override fun decodeProto(message: ProtoMessage?): List<Long> =
        message?.longList(1) ?: emptyList()
}

object ProtoOneLongListOrNull : ProtoDecoder<List<Long>?> {
    override fun decodeProto(message: ProtoMessage?): List<Long>? =
        message?.longListOrNull(1, 2)
}

// ---------------------------------------------------------------------------
// String
// ---------------------------------------------------------------------------

object ProtoOneString : ProtoDecoder<String> {
    override fun decodeProto(message: ProtoMessage?): String =
        message?.string(1) ?: ""
}

object ProtoOneStringOrNull : ProtoDecoder<String?> {
    override fun decodeProto(message: ProtoMessage?): String? =
        message?.stringOrNull(1, 2)
}

object ProtoOneStringList : ProtoDecoder<List<String>> {
    override fun decodeProto(message: ProtoMessage?): List<String> =
        message?.stringList(1) ?: emptyList()
}

object ProtoOneStringListOrNull : ProtoDecoder<List<String>?> {
    override fun decodeProto(message: ProtoMessage?): List<String>? =
        message?.stringListOrNull(1, 2)
}

// ---------------------------------------------------------------------------
// ByteArray
// ---------------------------------------------------------------------------

object ProtoOneByteArray : ProtoDecoder<ByteArray> {
    override fun decodeProto(message: ProtoMessage?): ByteArray =
        message?.byteArray(1) ?: ByteArray(0)
}

object ProtoOneByteArrayOrNull : ProtoDecoder<ByteArray?> {
    override fun decodeProto(message: ProtoMessage?): ByteArray? =
        message?.byteArrayOrNull(1, 2)
}

object ProtoOneByteArrayList : ProtoDecoder<List<ByteArray>> {
    override fun decodeProto(message: ProtoMessage?): List<ByteArray> =
        message?.byteArrayList(1) ?: emptyList()
}

object ProtoOneByteArrayListOrNull : ProtoDecoder<List<ByteArray>?> {
    override fun decodeProto(message: ProtoMessage?): List<ByteArray>? =
        message?.byteArrayListOrNull(1, 2)
}

// ---------------------------------------------------------------------------
// UUID
// ---------------------------------------------------------------------------

object ProtoOneUuid : ProtoDecoder<UUID<Any>> {
    override fun decodeProto(message: ProtoMessage?): UUID<Any> =
        message?.uuid(1) ?: UUID()
}

object ProtoOneUuidOrNull : ProtoDecoder<UUID<Any>?> {
    override fun decodeProto(message: ProtoMessage?): UUID<Any>? =
        message?.uuidOrNull(1, 2)
}

object ProtoOneUuidList : ProtoDecoder<List<UUID<Any>>> {
    override fun decodeProto(message: ProtoMessage?): List<UUID<Any>> =
        message?.uuidList(1) ?: emptyList()
}

object ProtoOneUuidListOrNull : ProtoDecoder<List<UUID<Any>>?> {
    override fun decodeProto(message: ProtoMessage?): List<UUID<Any>>? =
        message?.uuidListOrNull(1, 2)
}

// ---------------------------------------------------------------------------
// Instance
// ---------------------------------------------------------------------------

class ProtoOneInstance<T>(val decoder: ProtoDecoder<T>) : ProtoDecoder<T> {
    override fun decodeProto(message: ProtoMessage?): T =
        checkNotNull(message?.instance(1, decoder)) { "cannot decode instance with $decoder" }
}

class ProtoOneInstanceOrNull<T>(val decoder: ProtoDecoder<T>) : ProtoDecoder<T?> {
    override fun decodeProto(message: ProtoMessage?): T? =
        message?.instanceOrNull(1, 2, decoder)
}

class ProtoOneInstanceList<T>(val decoder: ProtoDecoder<T>) : ProtoDecoder<List<T>> {
    override fun decodeProto(message: ProtoMessage?): List<T> =
        message?.instanceList(1, decoder) ?: emptyList()
}

class ProtoOneInstanceListOrNull<T>(val decoder: ProtoDecoder<T>) : ProtoDecoder<List<T>?> {
    override fun decodeProto(message: ProtoMessage?): List<T>? =
        message?.instanceListOrNull(1, 2, decoder)
}

// ---------------------------------------------------------------------------
// Enum
// ---------------------------------------------------------------------------

class ProtoOneEnum<E : Enum<E>>(val entries : EnumEntries<E>) : ProtoDecoder<E> {
    override fun decodeProto(message: ProtoMessage?): E {
        if (message == null) return entries.first()
        return entries[message.int(1)]
    }
}

class ProtoOneEnumOrNull<E : Enum<E>>(val entries : EnumEntries<E>) : ProtoDecoder<E?> {
    override fun decodeProto(message: ProtoMessage?): E? {
        if (message == null) return null
        return message.intOrNull(1, 2)?.let { entries[it] }
    }
}

class ProtoOneEnumList<E: Enum<E>>(val entries : EnumEntries<E>) : ProtoDecoder<List<E>> {
    override fun decodeProto(message: ProtoMessage?): List<E> {
        if (message == null) return emptyList()
        return message.intList(1).map { entries[it] }
    }
}

class ProtoOneEnumListOrNull<E : Enum<E>>(val entries : EnumEntries<E>) : ProtoDecoder<List<E>?> {
    override fun decodeProto(message: ProtoMessage?): List<E>? {
        if (message == null) return null
        return message.intListOrNull(1, 2)?.map { entries[it] }
    }
}