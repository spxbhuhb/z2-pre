package hu.simplexion.z2.serialization.protobuf

interface ProtoEncoder<T> {

    fun encodeProto(value: T) : ByteArray

}