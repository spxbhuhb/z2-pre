@file:Suppress("PropertyName")

package hu.simplexion.z2.commons.i18n

import hu.simplexion.z2.commons.util.PublicApi
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

/**
 * Stores textual information that should be translated when the UI is displayed on a
 * different language.
 */
@PublicApi
abstract class LocalizedTextStore {

    val _map = mutableMapOf<String, LocalizedText>()
    val _support = mutableMapOf<String, LocalizedTextSupport>()

    class LocalizedTextDelegate : ReadOnlyProperty<LocalizedTextStore, LocalizedText> {
        override fun getValue(thisRef: LocalizedTextStore, property: KProperty<*>): LocalizedText {
            return thisRef._map[property.name] !!
        }
    }

    fun LocalizedText.support(text : String) : Pair<LocalizedText,String> {
        return this to text
    }

    operator fun String.provideDelegate(thisRef: LocalizedTextStore, prop: KProperty<*>): ReadOnlyProperty<LocalizedTextStore, LocalizedText> {
        thisRef._map[prop.name] = BasicLocalizedText(prop.name, this, thisRef)
        return LocalizedTextDelegate()
    }

    operator fun Pair<LocalizedText,String>.provideDelegate(thisRef: LocalizedTextStore, prop: KProperty<*>): ReadOnlyProperty<LocalizedTextStore, LocalizedText> {
        BasicLocalizedTextSupport(prop.name, this.first.key, this.second).also {
            thisRef._map[prop.name] = it
            thisRef._support[it.supportFor] = it
        }
        return LocalizedTextDelegate()
    }
}