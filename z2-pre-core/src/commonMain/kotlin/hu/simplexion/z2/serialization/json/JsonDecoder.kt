package hu.simplexion.z2.serialization.json

import hu.simplexion.z2.serialization.json.elements.JsonElement

interface JsonDecoder<T> {

    fun decodeJson(element : JsonElement?) : T

}