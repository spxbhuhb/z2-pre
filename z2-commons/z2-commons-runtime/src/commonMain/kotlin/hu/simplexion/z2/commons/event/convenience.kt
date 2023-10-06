package hu.simplexion.z2.commons.event

import hu.simplexion.z2.commons.util.Z2Handle
import kotlin.reflect.KProperty

inline fun <reified T> recalculateOn(handle: Z2Handle, noinline builder: (curVal: T?) -> T) =
    ReactiveValue(handle, builder)

operator fun <T> ReactiveValue<T>.getValue(thisRef: Any?, property: KProperty<*>): T {
    return checkNotNull(curVal) { "access before initialization thisRef: $thisRef  property: ${property.name}" }
}