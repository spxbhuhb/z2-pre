package hu.simplexion.z2.commons.protobuf

interface ProtoDecoder<T> {

    fun decodeProto(message : ProtoMessage?) : T

}