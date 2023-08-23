package hu.simplexion.z2.commons.protobuf

import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlin.time.Duration
import kotlin.time.Duration.Companion.nanoseconds


object ProtoDuration : ProtoDecoder<Duration>, ProtoEncoder<Duration> {

    override fun decodeProto(message: ProtoMessage?): Duration {
        if (message == null) return Duration.ZERO
        return message.long(1).nanoseconds
    }

    override fun encodeProto(value: Duration): ByteArray =
        ProtoMessageBuilder()
            .long(1, value.inWholeNanoseconds)
            .pack()

}

object ProtoInstant : ProtoDecoder<Instant>, ProtoEncoder<Instant> {

    override fun decodeProto(message: ProtoMessage?): Instant {
        if (message == null) return Instant.DISTANT_PAST
        return Instant.fromEpochSeconds(message.long(1), message.int(2))
    }

    override fun encodeProto(value: Instant): ByteArray =
        ProtoMessageBuilder()
            .long(1, value.epochSeconds)
            .int(2, value.nanosecondsOfSecond)
            .pack()

}

object ProtoLocalDate : ProtoDecoder<LocalDate>, ProtoEncoder<LocalDate> {

    override fun decodeProto(message: ProtoMessage?): LocalDate {
        if (message == null) return LocalDate.fromEpochDays(0)
        return LocalDate(
            message.int(1),
            message.int(2),
            message.int(3)
        )
    }

    override fun encodeProto(value: LocalDate): ByteArray =
        ProtoMessageBuilder()
            .int(1, value.year)
            .int(2, value.monthNumber)
            .int(3, value.dayOfMonth)
            .pack()

}

object ProtoLocalDateTime : ProtoDecoder<LocalDateTime>, ProtoEncoder<LocalDateTime> {

    override fun decodeProto(message: ProtoMessage?): LocalDateTime {
        if (message == null) return LocalDateTime(0,1,1,0,0,0,0)
        return LocalDateTime(
            message.int(1),
            message.int(2),
            message.int(3),
            message.int(4),
            message.int(5),
            message.int(6),
            message.int(7)
        )
    }

    override fun encodeProto(value: LocalDateTime): ByteArray =
        ProtoMessageBuilder()
            .int(1, value.year)
            .int(2, value.monthNumber)
            .int(3, value.dayOfMonth)
            .int(4, value.hour)
            .int(5, value.minute)
            .int(6, value.second)
            .int(7, value.nanosecond)
            .pack()

}