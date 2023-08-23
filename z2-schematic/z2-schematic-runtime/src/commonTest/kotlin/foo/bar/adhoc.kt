package foo.bar

import hu.simplexion.z2.commons.protobuf.ProtoMessage
import hu.simplexion.z2.schematic.runtime.Schematic
import hu.simplexion.z2.schematic.runtime.SchematicCompanion

class Language : Schematic<Language>() {
    var id by uuid<Language>()
    var isoCode by string(minLength = 2, maxLength = 2)
    var countryCode by string(minLength = 2, maxLength = 2, pattern = Regex("[A-Z]{2}"))
    var nativeName by string(minLength = 2, maxLength = 30, blank = false)
    var visible by boolean(default = false)

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

    return "OK"
}