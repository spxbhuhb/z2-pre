package hu.simplexion.z2.commons.i18n

import hu.simplexion.z2.commons.util.PublicApi
import hu.simplexion.z2.commons.util.UUID
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

@PublicApi
open class LocalizedTextStore(
    @PublicApi
    val uuid : UUID<LocalizedTextStore>
) {
    val map = mutableMapOf<String, LocalizedText>()
    val support = mutableMapOf<String, LocalizedTextSupport>()

    class LocalizedTextDelegate : ReadOnlyProperty<LocalizedTextStore, LocalizedText> {
        override fun getValue(thisRef: LocalizedTextStore, property: KProperty<*>): LocalizedText {
            return thisRef.map[property.name] !!
        }
    }

    fun LocalizedText.support(text : String) : Pair<LocalizedText,String> {
        return this to text
    }

    operator fun String.provideDelegate(thisRef: LocalizedTextStore, prop: KProperty<*>): ReadOnlyProperty<LocalizedTextStore, LocalizedText> {
        thisRef.map[prop.name] = BasicLocalizedText(prop.name, this, thisRef)
        return LocalizedTextDelegate()
    }

    operator fun Pair<LocalizedText,String>.provideDelegate(thisRef: LocalizedTextStore, prop: KProperty<*>): ReadOnlyProperty<LocalizedTextStore, LocalizedText> {
        BasicLocalizedTextSupport(prop.name, this.first.key, this.second).also {
            thisRef.map[prop.name] = it
            thisRef.support[it.supportFor] = it
        }
        return LocalizedTextDelegate()
    }
}