package hu.simplexion.z2.browser.routing

import hu.simplexion.z2.util.UUID
import hu.simplexion.z2.util.UUID.Companion.toUuidOrNull

interface RouterParameter<T> {
    var valueOrNull : T?

    val value
        get() = requireNotNull(valueOrNull)

    fun set(stringValue : String)
}

open class IntRouterParameter : RouterParameter<Int> {

    override var valueOrNull : Int? = null

    override fun set(stringValue: String) {
        valueOrNull = stringValue.toIntOrNull()
    }
}

open class StringRouterParameter : RouterParameter<String> {

    override var valueOrNull: String? = null

    override fun set(stringValue: String) {
        valueOrNull = stringValue
    }
}

open class UuidRouterParameter<T> : RouterParameter<UUID<T>> {

    override var valueOrNull: UUID<T>? = null

    override fun set(stringValue: String) {
        valueOrNull = stringValue.toUuidOrNull()
    }

}