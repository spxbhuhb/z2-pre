package hu.simplexion.z2.setting.dsl

import hu.simplexion.z2.auth.model.Principal
import hu.simplexion.z2.setting.implementation.SettingImpl
import hu.simplexion.z2.util.UUID
import kotlin.reflect.KProperty

data class SettingDelegate<T>(
    val path: String,
    val owner: UUID<Principal>,
    val sensitive: Boolean = false,
    val encoder: (value: T) -> String,
    val decoder: (value: String?) -> T
) {

    var value: T? = null

    operator fun getValue(thisRef: Any?, property: KProperty<*>): T {
        if (value == null) {
            value = SettingImpl.rootProvider.get(owner, path).firstOrNull()?.value?.let { decoder(it) }
        }
        return value!!
    }

    operator fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
        this.value = value
        SettingImpl.rootProvider.put(owner, path, encoder(value))
    }

}