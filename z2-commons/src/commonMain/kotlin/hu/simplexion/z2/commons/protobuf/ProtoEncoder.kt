package hu.simplexion.z2.commons.protobuf

interface ProtoEncoder<T> {

    fun encodeProto(value: T) : ByteArray

}