package hu.simplexion.z2.service.transport

import hu.simplexion.z2.commons.protobuf.ProtoDecoder
import hu.simplexion.z2.commons.protobuf.ProtoMessage
import hu.simplexion.z2.commons.protobuf.ProtoMessageBuilder
import hu.simplexion.z2.service.BasicServiceContext
import hu.simplexion.z2.service.defaultServiceImplFactory

/**
 * Get the service from the implementation factory, execute the function with it and
 * returns with the result.
 */
class LocalServiceCallTransport : ServiceCallTransport {

    companion object {
        // FIXME local service context UUID
        val localServiceContext = BasicServiceContext()
    }

    override suspend fun <T> call(serviceName: String, funName: String, payload: ByteArray, decoder: ProtoDecoder<T>): T {

        val service = requireNotNull(defaultServiceImplFactory[serviceName, localServiceContext])

        val responseBuilder = ProtoMessageBuilder()

        service.dispatch(funName, ProtoMessage(payload), responseBuilder)

        return decoder.decodeProto(ProtoMessage(responseBuilder.pack()))
    }

}