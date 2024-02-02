package foo.bar

import hu.simplexion.z2.schematic.Schematic
import hu.simplexion.z2.schematic.SchematicCompanion
import hu.simplexion.z2.schematic.schema.SchemaFieldType
import hu.simplexion.z2.serialization.protobuf.ProtoMessage
import kotlin.time.Duration

class Language : Schematic<Language>() {
    var uuid by uuid<Language>()

    var duration by duration()

    var isoCode by string() minLength 2 maxLength 2
    var countryCode by string() minLength 2 maxLength 2 pattern Regex("[A-Z]{2}")
    var nativeName by string() minLength 2 maxLength 30 blank false
    var visible by boolean().nullable()

    var parent by schematic<Language>().nullable()
    var type by enum<SchemaFieldType>()

    companion object : SchematicCompanion<Language> {
        override fun decodeProto(message: ProtoMessage?): Language {
            TODO()
        }
    }
}

fun box(): String {
    val l = Language().apply {
        isoCode = "hu"
        countryCode = "HU"
        nativeName = "Magyar"
        visible = true
    }

    val bytes = l.schematicCompanion.encodeProto(l)
    val l2 = l.schematicCompanion.decodeProto(ProtoMessage(bytes))

    if (l.duration != Duration.ZERO) return "Fail"
    return "OK"
}