package hu.simplexion.z2.commons.protobuf

import hu.simplexion.z2.serialization.protobuf.*
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.time.Duration.Companion.seconds

class DatetimeTest {

    @Test
    fun testDuration() {
        val expected = 10.seconds
        val wireformat = ProtoMessageBuilder().instance(1, ProtoDuration, expected).pack()
        val actual = ProtoMessage(wireformat).instance(1, ProtoDuration)
        assertEquals(expected, actual)
    }

    @Test
    fun testInstant() {
        val expected = Clock.System.now()
        val wireformat = ProtoMessageBuilder().instance(1, ProtoInstant, expected).pack()
        val actual = ProtoMessage(wireformat).instance(1, ProtoInstant)
        assertEquals(expected, actual)
    }

    @Test
    fun testLocalDate() {
        val expected = LocalDate(2023,7,27)
        val wireformat = ProtoMessageBuilder().instance(1, ProtoLocalDate, expected).pack()
        val actual = ProtoMessage(wireformat).instance(1, ProtoLocalDate)
        assertEquals(expected, actual)
    }

    @Test
    fun testLocalDateTime() {
        val expected = LocalDateTime(2023,7,27,15,35,5,11)
        val wireformat = ProtoMessageBuilder().instance(1, ProtoLocalDateTime, expected).pack()
        val actual = ProtoMessage(wireformat).instance(1, ProtoLocalDateTime)
        assertEquals(expected, actual)
    }
}