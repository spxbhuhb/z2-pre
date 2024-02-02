package hu.simplexion.z2.schematic

import hu.simplexion.z2.schematic.schema.Schema
import hu.simplexion.z2.serialization.protobuf.ProtoDecoder
import hu.simplexion.z2.serialization.protobuf.ProtoEncoder
import hu.simplexion.z2.serialization.protobuf.ProtoMessage

interface SchematicCompanion<T : Schematic<T>> : ProtoEncoder<T>, ProtoDecoder<T> {

    val schematicSchema : Schema<T>
        get() = placeholder()

    fun newInstance() : T = placeholder()

    override fun decodeProto(message: ProtoMessage?): T = placeholder()

    override fun encodeProto(value: T): ByteArray = placeholder()

    operator fun invoke(builder : T.() -> Unit) : T =
        newInstance().apply(builder)

    fun setFieldValue(instance : T, name : String, value : Any?) : Unit = placeholder()

    fun getFieldValue(instance : T, name : String) : Any? = placeholder()

}