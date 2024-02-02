package hu.simplexion.z2.serialization.json

interface JsonEncoder<T> {

    fun encodeJson(writer : JsonBufferWriter, value: T)

}