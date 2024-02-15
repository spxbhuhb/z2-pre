package hu.simplexion.z2.setting.dsl

import hu.simplexion.z2.auth.model.Principal
import hu.simplexion.z2.setting.implementation.SettingImpl
import hu.simplexion.z2.util.UUID
import kotlin.reflect.KProperty

actual class SettingDelegate<T>(
    actual val path: String,
    actual val owner: UUID<Principal>?,
    val encoder: (value: T) -> String,
    val decoder: (value: String?) -> T
) {

    actual var sensitive: Boolean = false
    actual var writable: Boolean = true

    // FIXME settings default mess
    var default : T? = null

    actual var valueOrNull: T? = null

    actual var value : T
        get() = requireNotNull(valueOrNull) { "missing setting: path=$path owner=$owner" }
        set(value) { valueOrNull = value }

    actual operator fun getValue(thisRef: Any?, property: KProperty<*>): T {
        valueOrNull = SettingImpl.rootProvider.get(owner, path).firstOrNull()?.value?.let { decoder(it) }
        return requireNotNull(valueOrNull ?: default) { "missing setting: path=$path owner=$owner" }
    }

    actual operator fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
        this.value = value
        SettingImpl.rootProvider.put(owner, path, encoder(value))
    }

    actual infix fun default(value : T) : SettingDelegate<T> {
        this.default = value
        return this
    }

    actual operator fun rangeTo(settingFlag: SettingFlag) : SettingDelegate<T> {
        when (settingFlag) {
            SettingFlag.sensitive -> sensitive = true
            SettingFlag.writable -> writable = true
        }
        return this
    }

}