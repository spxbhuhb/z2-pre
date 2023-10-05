package hu.simplexion.z2.commons.i18n

import hu.simplexion.z2.commons.util.PublicApi
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

@PublicApi
open class LocalizedIconStore {

    val _map: MutableMap<String, LocalizedIcon> = mutableMapOf()

    class LocalizedTextDelegate : ReadOnlyProperty<LocalizedIconStore, LocalizedIcon> {
        override fun getValue(thisRef: LocalizedIconStore, property: KProperty<*>): LocalizedIcon {
            return thisRef._map[property.name] !!
        }
    }

    operator fun String.provideDelegate(thisRef: LocalizedIconStore, prop: KProperty<*>): ReadOnlyProperty<LocalizedIconStore, LocalizedIcon> {
        thisRef._map[prop.name] = BasicLocalizedIcon(prop.name, this)
        return LocalizedTextDelegate()
    }
}