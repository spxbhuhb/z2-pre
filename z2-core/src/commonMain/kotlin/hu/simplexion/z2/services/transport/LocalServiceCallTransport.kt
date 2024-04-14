package hu.simplexion.z2.services.transport

import hu.simplexion.z2.serialization.Message
import hu.simplexion.z2.serialization.SerializationConfig
import hu.simplexion.z2.services.BasicServiceContext
import hu.simplexion.z2.services.defaultServiceImplFactory

/**
 * Get the service from the implementation factory, execute the function with it and
 * returns with the result.
 */
class LocalServiceCallTransport : ServiceCallTransport {

    companion object {
        // FIXME local service context UUID
        val localServiceContext = BasicServiceContext()
    }

    val serializationConfig
        get() = SerializationConfig.defaultSerialization

    override suspend fun call(serviceName: String, funName: String, payload: ByteArray): Message {

        val service = requireNotNull(defaultServiceImplFactory[serviceName, localServiceContext])

        val responseBuilder = serializationConfig.messageBuilder()

        service.dispatch(funName, serializationConfig.toMessage(payload), responseBuilder)

        return serializationConfig.toMessage(responseBuilder.pack())
    }

}