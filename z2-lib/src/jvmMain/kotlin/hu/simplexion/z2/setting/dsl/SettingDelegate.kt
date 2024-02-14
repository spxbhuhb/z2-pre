package hu.simplexion.z2.setting.dsl

import hu.simplexion.z2.auth.model.Principal
import hu.simplexion.z2.setting.implementation.SettingImpl
import hu.simplexion.z2.util.UUID
import kotlin.reflect.KProperty

data class SettingDelegate<T>(
    val path: String,
    val owner: UUID<Principal>,
    val encoder: (value: T) -> String,
    val decoder: (value: String?) -> T
) {

    var sensitive: Boolean = false
    var writable: Boolean = true
    var initialized : Boolean = false

    var valueOrNull: T? = null

    var value : T
        get() = requireNotNull(valueOrNull) { "missing setting: path=$path owner=$owner" }
        set(value) { valueOrNull = value }

    operator fun getValue(thisRef: Any?, property: KProperty<*>): T {
        if (!initialized) {
            valueOrNull = SettingImpl.rootProvider.get(owner, path).firstOrNull()?.value?.let { decoder(it) }
        }
        return value
    }

    operator fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
        this.value = value
        SettingImpl.rootProvider.put(owner, path, encoder(value))
    }

    infix fun default(value : T) : SettingDelegate<T> {
        this.value = value
        return this
    }

    operator fun rangeTo(settingFlag: SettingFlag) : SettingDelegate<T> {
        when (settingFlag) {
            SettingFlag.sensitive -> sensitive = true
            SettingFlag.writable -> writable = true
        }
        return this
    }

}