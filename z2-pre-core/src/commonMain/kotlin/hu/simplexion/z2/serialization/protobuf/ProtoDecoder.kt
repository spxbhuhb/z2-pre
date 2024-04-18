package hu.simplexion.z2.serialization.protobuf

interface ProtoDecoder<T> {

    fun decodeProto(message : ProtoMessage?) : T

}