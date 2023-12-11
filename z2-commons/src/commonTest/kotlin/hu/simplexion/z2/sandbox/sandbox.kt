package hu.simplexion.z2.sandbox

import kotlin.reflect.KProperty


object Constraint
object Attribute

class StringFieldBuilder {

    val constraint = Constraint
    val attribute = Attribute

    infix fun Constraint.maxLength(value: Int) : Constraint {
        return this
    }

    infix fun Constraint.minLength(value: Int) : Constraint {
        return this
    }

    infix fun Attribute.label(value: String) : Attribute {
       return this
    }

    infix fun Attribute.default(value: String) : Attribute {
        return this
    }

}

class Field {
    var value : String = ""

    operator fun getValue(thisRef: Any?, property: KProperty<*>): String {
        return value
    }

    operator fun setValue(thisRef: Any?, property: KProperty<*>, value: String) {
       this.value = value
    }
}

class OptField {
    var value : String? = null

    operator fun getValue(thisRef: Any?, property: KProperty<*>): String? {
        return value
    }

    operator fun setValue(thisRef: Any?, property: KProperty<*>, value: String?) {
        this.value = value
    }
}

fun string(b: StringFieldBuilder.() -> Unit) = Field()

fun optString(b: StringFieldBuilder.() -> Unit) = OptField()

class A {

    constructor() { }

    constructor(inS : String, inOs : String) : this() {
        // s = inS
        os = inOs
    }

    val s by string {
        constraint maxLength 12 minLength 23
        attribute label "Hello"
        attribute default "12"
    }

    var os by optString {
        constraint maxLength 12 minLength 23
        attribute label "Hello"
        attribute default "12"
    }

}