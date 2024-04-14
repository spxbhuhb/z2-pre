package hu.simplexion.z2.services.transport

import hu.simplexion.z2.serialization.Message
import hu.simplexion.z2.serialization.SerializationConfig
import hu.simplexion.z2.serialization.protobuf.ProtoMessage
import hu.simplexion.z2.services.BasicServiceContext
import hu.simplexion.z2.services.ServiceImpl
import hu.simplexion.z2.services.defaultServiceImplFactory

/**
 * Service call transport that uses the given implementation directly, ignoring [defaultServiceImplFactory].
 *
 * This class is intended for testing. For direct calls using the implementation directly is usually better
 * as it avoids the encoding and decoding overhead.
 */
class DirectServiceCallTransport(
    val implementation: ServiceImpl<*>
) : ServiceCallTransport {

    companion object {
        // FIXME local service context UUID
        val directServiceContext = BasicServiceContext()
    }

    override suspend fun call(serviceName: String, funName: String, payload: ByteArray): Message {
        val responseBuilder = SerializationConfig.defaultSerialization.messageBuilder()

        implementation.newInstance(directServiceContext).dispatch(funName, ProtoMessage(payload), responseBuilder)

        return SerializationConfig.defaultSerialization.toMessage(responseBuilder.pack())

    }

}