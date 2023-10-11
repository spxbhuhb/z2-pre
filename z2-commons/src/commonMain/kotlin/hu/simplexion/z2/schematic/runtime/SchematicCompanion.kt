package hu.simplexion.z2.schematic.runtime

import hu.simplexion.z2.commons.protobuf.ProtoDecoder
import hu.simplexion.z2.commons.protobuf.ProtoEncoder
import hu.simplexion.z2.commons.protobuf.ProtoMessage
import hu.simplexion.z2.schematic.runtime.schema.Schema

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