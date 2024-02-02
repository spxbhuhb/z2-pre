package hu.simplexion.z2.services.transport

import hu.simplexion.z2.serialization.protobuf.ProtoDecoder

interface ServiceCallTransport {

    suspend fun <T> call(serviceName : String, funName : String, payload: ByteArray, decoder : ProtoDecoder<T>): T

}