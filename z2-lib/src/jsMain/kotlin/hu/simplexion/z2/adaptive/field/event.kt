package hu.simplexion.z2.adaptive.field

import hu.simplexion.z2.adaptive.event.Z2Event
import hu.simplexion.z2.util.Z2Handle

interface FieldKeyEvent : Z2Event

class EnterKeyEvent(override val busHandle: Z2Handle) : FieldKeyEvent

class EscapeKeyEvent(override val busHandle: Z2Handle) : FieldKeyEvent