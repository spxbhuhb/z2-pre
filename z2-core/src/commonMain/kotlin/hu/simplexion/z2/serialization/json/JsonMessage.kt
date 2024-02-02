package hu.simplexion.z2.serialization.json

import hu.simplexion.z2.serialization.json.elements.JsonArray
import hu.simplexion.z2.serialization.json.elements.JsonBoolean
import hu.simplexion.z2.serialization.json.elements.JsonElement
import hu.simplexion.z2.serialization.json.elements.JsonObject
import hu.simplexion.z2.util.UUID

/**
 * Parse JSON messages.
 *
 * @param  wireFormat  The wire format message to parse.
 */
class JsonMessage(
    wireFormat: ByteArray,
    offset: Int = 0,
    length: Int = wireFormat.size
) {

    val root = JsonBufferReader(wireFormat, offset, length).read()
    val map = (root as? JsonObject)?.entries

    fun get(fieldName: String): JsonElement = requireNotNull(map?.get(fieldName)) { "missing field: $fieldName" }

    fun getOrNull(fieldName: String): JsonElement? = map?.get(fieldName)

    // -----------------------------------------------------------------------------------------
    // Boolean
    // -----------------------------------------------------------------------------------------

    fun boolean(fieldName: String): Boolean =
        (get(fieldName) as JsonBoolean).value

    fun booleanOrNull(fieldName: String): Boolean? =
        (getOrNull(fieldName) as? JsonBoolean)?.value

    fun booleanList(fieldName: String) =
        requireNotNull(booleanListOrNull(fieldName)) { "missing or null array" }

    fun booleanListOrNull(fieldName: String): List<Boolean>? =
        array(fieldName) { (it as JsonBoolean).value }

    // -----------------------------------------------------------------------------------------
    // Int
    // -----------------------------------------------------------------------------------------

    fun int(fieldName: String): Int = get(fieldName).asInt

    fun intOrNull(fieldName: String): Int? =
        getOrNull(fieldName)?.asInt

    fun intList(fieldName: String) =
        requireNotNull(intListOrNull(fieldName)) { "missing or null array" }

    fun intListOrNull(fieldName: String): List<Int>? =
        array(fieldName) { it.asInt }

    // -----------------------------------------------------------------------------------------
    // Long
    // -----------------------------------------------------------------------------------------
    
    fun long(fieldName: String): Long = get(fieldName).asLong

    fun longOrNull(fieldName: String): Long? =
        getOrNull(fieldName)?.asLong

    fun longList(fieldName: String) =
        requireNotNull(longListOrNull(fieldName)) { "missing or null array" }

    fun longListOrNull(fieldName: String): List<Long>? =
        array(fieldName) { it.asLong }

    // -----------------------------------------------------------------------------------------
    // String
    // -----------------------------------------------------------------------------------------

    fun string(fieldName: String): String = get(fieldName).toString()

    fun stringOrNull(fieldName: String): String? =
        getOrNull(fieldName)?.toString()

    fun stringList(fieldName: String) =
        requireNotNull(stringListOrNull(fieldName)) { "missing or null array" }

    fun stringListOrNull(fieldName: String): List<String>? =
        array(fieldName) { it.toString() }

    // -----------------------------------------------------------------------------------------
    // ByteArray
    // -----------------------------------------------------------------------------------------

    fun byteArray(fieldName: String): ByteArray = get(fieldName).asByteArray

    fun byteArrayOrNull(fieldName: String): ByteArray? =
        getOrNull(fieldName)?.asByteArray

    fun byteArrayList(fieldName: String) =
        requireNotNull(byteArrayListOrNull(fieldName)) { "missing or null array" }

    fun byteArrayListOrNull(fieldName: String): List<ByteArray>? =
        array(fieldName) { it.asByteArray }

    // -----------------------------------------------------------------------------------------
    // UUID
    // -----------------------------------------------------------------------------------------

    fun uuid(fieldName: String): UUID<*> = get(fieldName).asUuid

    fun uuidOrNull(fieldName: String): UUID<*>? =
        getOrNull(fieldName)?.asUuid

    fun uuidList(fieldName: String) =
        requireNotNull(uuidListOrNull(fieldName)) { "missing or null array" }

    fun uuidListOrNull(fieldName: String): List<UUID<*>>? =
        array(fieldName) { it.asUuid }

    // -----------------------------------------------------------------------------------------
    // Instance
    // -----------------------------------------------------------------------------------------

    fun <T> instance(fieldName: String, decoder: JsonDecoder<T>): T =
        decoder.decodeJson(get(fieldName))

    fun <T> instanceOrNull(fieldName: String, nullfieldName: String, decoder: JsonDecoder<T>): T? =
        getOrNull(nullfieldName)?.let { decoder.decodeJson(it) }

    fun <T> instanceList(fieldName: String, decoder: JsonDecoder<T>): List<T> =
        requireNotNull(instanceListOrNull(fieldName, decoder)) { "missing or null instance" }

    fun <T> instanceListOrNull(fieldName: String, decoder: JsonDecoder<T>): List<T>? =
        array(fieldName) { decoder.decodeJson(it) }

    // --------------------------------------------------------------------------------------
    // Helpers
    // --------------------------------------------------------------------------------------

    fun <T> array(
        fieldName: String,
        item: (element: JsonElement) -> T
    ): MutableList<T>? {
        val result = mutableListOf<T>()

        val a = getOrNull(fieldName) ?: return null
        require(a is JsonArray) { "field $fieldName is not an array" }

        for (element in a.items) {
            result += item(element)
        }

        return result
    }

}