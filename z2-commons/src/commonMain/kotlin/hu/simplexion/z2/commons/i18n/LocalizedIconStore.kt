package hu.simplexion.z2.commons.i18n

import hu.simplexion.z2.commons.util.PublicApi
import hu.simplexion.z2.commons.util.UUID
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

@PublicApi
open class LocalizedIconStore(
    @PublicApi
    val uuid : UUID<LocalizedIconStore>
) {
    val map: MutableMap<String, LocalizedIcon> = mutableMapOf()

    class LocalizedTextDelegate : ReadOnlyProperty<LocalizedIconStore, LocalizedIcon> {
        override fun getValue(thisRef: LocalizedIconStore, property: KProperty<*>): LocalizedIcon {
            return thisRef.map[property.name] !!
        }
    }

    operator fun String.provideDelegate(thisRef: LocalizedIconStore, prop: KProperty<*>): ReadOnlyProperty<LocalizedIconStore, LocalizedIcon> {
        thisRef.map[prop.name] = BasicLocalizedIcon(prop.name, this)
        return LocalizedTextDelegate()
    }
}