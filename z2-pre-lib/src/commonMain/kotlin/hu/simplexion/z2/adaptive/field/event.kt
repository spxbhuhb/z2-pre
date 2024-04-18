package hu.simplexion.z2.adaptive.field

import hu.simplexion.z2.deprecated.event.Z2Event
import hu.simplexion.z2.schematic.Schematic
import hu.simplexion.z2.schematic.SchematicEvent
import hu.simplexion.z2.util.Z2Handle

interface FieldKeyEvent : Z2Event

class EnterKeyEvent(override val busHandle: Z2Handle) : FieldKeyEvent

class EscapeKeyEvent(override val busHandle: Z2Handle) : FieldKeyEvent

interface FieldRequestEvent : Z2Event

class RequestBlurEvent(override val busHandle: Z2Handle) : FieldRequestEvent

fun Z2Event.isOf(schematic : Schematic<*>) =
    this is SchematicEvent && this.schematic == schematic