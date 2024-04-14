package hu.simplexion.z2.services.transport

import hu.simplexion.z2.serialization.InstanceDecoder
import hu.simplexion.z2.serialization.InstanceEncoder
import hu.simplexion.z2.serialization.Message
import hu.simplexion.z2.serialization.MessageBuilder
import hu.simplexion.z2.serialization.protobuf.ProtoMessage
import hu.simplexion.z2.serialization.protobuf.dumpProto
import hu.simplexion.z2.util.UUID

/**
 * Envelope a response payload.
 *
 * @property  [callId]   Call id from the corresponding [RequestEnvelope].
 * @property  [status]   Result status of the call.
 * @property  [payload]  Return value of the called service function.
 */
class ResponseEnvelope(
    val callId: UUID<RequestEnvelope>,
    val status: ServiceCallStatus,
    val payload: ByteArray,
) {

    companion object : InstanceEncoder<ResponseEnvelope>, InstanceDecoder<ResponseEnvelope> {

        override fun decodeInstance(message: Message?): ResponseEnvelope {
            requireNotNull(message)
            return ResponseEnvelope(
                message.uuid(1, "callId"),
                requireNotNull(message.int(2, "status")).let { mv -> ServiceCallStatus.entries.first { it.value == mv } },
                message.byteArray(3, "payload")
            )
        }

        override fun encodeInstance(builder: MessageBuilder, value: ResponseEnvelope): ByteArray =
            builder
                .uuid(1, "callId", value.callId)
                .int(2, "status", value.status.value)
                .byteArray(3, "payload", value.payload)
                .pack()
    }

    fun dump(separator : String = "\n") : String {
        val result = mutableListOf<String>()
        result += "callId: $callId, status: $status"
        ProtoMessage(payload).dumpProto(result)
        return result.joinToString(separator)
    }
}