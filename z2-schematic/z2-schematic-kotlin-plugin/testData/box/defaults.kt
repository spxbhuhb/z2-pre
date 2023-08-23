package foo.bar

import hu.simplexion.z2.schematic.runtime.Schematic
import hu.simplexion.z2.commons.util.UUID

enum class E {
    V1,
    V2
}

class A : Schematic<A>() {
    val intField by int(default = 123)
}

class Test : Schematic<Test>() {
    val boolean by boolean() // false
    val booleanWithDefault by boolean(default = true) // true
    val booleanNullDefault by boolean().nullable() // null
    val booleanNullWithDefaultFalse by boolean(default = false).nullable() // false
    val booleanNullWithDefaultTrue by boolean(default = true).nullable() // true

    val enum by enum(E.values())
    val enumWithDefault by enum(E.values(), default = E.V2)

    val int by int()
    val intWithDefault by int(default = 5)

    val schematic by schematic<A>()
    val schematicNullDefault by schematic<A>().nullable()

    val string by string()
    val stringDefault by string(default = "abc")

    val uuid by uuid<Test>()
    val uuidDefault by uuid<Test>(default = UUID<Test>("3ba47aa1-a8dd-4203-b5f0-e891867c0855"))
}

fun box(): String {
    val test = Test()

    if (test.boolean) return "Fail"
    if (!test.booleanWithDefault) return "Fail"
    if (test.booleanNullDefault != null) return "Fail"
    if (test.booleanNullWithDefaultFalse != false) return "Fail"
    if (test.booleanNullWithDefaultTrue != true) return "Fail"

    if (test.enum != E.V1) return "Fail: enum"
    if (test.enumWithDefault != E.V2) return "Fail: enumWithDefault"

    if (test.int != 0) return "Fail"
    if (test.intWithDefault != 5) return "Fail"

    if (test.schematic.intField != 123) return "Fail"
    if (test.schematicNullDefault != null) return "Fail"

    if (test.string != "") return "Fail"
    if (test.stringDefault != "abc") return "Fail"

    if (test.uuid != UUID.nil<Test>()) return "Fail"
    if (test.uuidDefault != UUID<Test>("3ba47aa1-a8dd-4203-b5f0-e891867c0855")) return "Fail"

    return "OK"
}