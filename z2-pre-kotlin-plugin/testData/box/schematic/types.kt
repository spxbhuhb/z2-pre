package foo.bar

import hu.simplexion.z2.schematic.schema.Schema
import hu.simplexion.z2.schematic.schema.field.IntSchemaField
import hu.simplexion.z2.schematic.Schematic
import kotlin.time.Duration

enum class E {
    V1,
    V2
}

class A : Schematic<A>() {
    var intField by int()
}

class Test : Schematic<Test>() {

    var booleanField by boolean()
    var durationField by duration()
    var enumField by enum<E>()
    var emailField by email()
    var instantField by instant()
    var intField by int()
    var localDateField by localDate()
    var localDateTimeField by localDateTime()
    var longField by long()
    var phoneField by phoneNumber()

    var schematicField by schematic<A>()
    var schematicListField by schematic<Test>().nullable()

    var secretField by secret()

    var stringField by string()
    var stringListField by stringList()

    var uuidField by uuid<Test>()
    var uuidListField by uuidList<Test>()
}

fun box(): String {
    val test = Test()
    if (test.durationField != Duration.ZERO) return "Fail: duration"
    return "OK"
}