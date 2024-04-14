package hu.simplexion.z2.services.transport

import hu.simplexion.z2.serialization.InstanceDecoder
import hu.simplexion.z2.serialization.InstanceEncoder
import hu.simplexion.z2.serialization.Message
import hu.simplexion.z2.serialization.MessageBuilder
import hu.simplexion.z2.util.UUID

class RequestEnvelope(
    val callId: UUID<RequestEnvelope>,
    val serviceName: String,
    val funName: String,
    val payload: ByteArray
) {

    companion object : InstanceDecoder<RequestEnvelope>, InstanceEncoder<RequestEnvelope> {

        override fun decodeInstance(message: Message?): RequestEnvelope {
            requireNotNull(message)
            return RequestEnvelope(
                message.uuid(1, "callId"),
                message.string(2, "serviceName"),
                message.string(3, "funName"),
                message.byteArray(4, "payload")
            )
        }

        override fun encodeInstance(builder: MessageBuilder, value: RequestEnvelope): ByteArray =
            builder
                .uuid(1, "callId", value.callId)
                .string(2, "serviceName", value.serviceName)
                .string(3, "funName", value.funName)
                .byteArray(4, "payLoad", value.payload)
                .pack()
    }


}