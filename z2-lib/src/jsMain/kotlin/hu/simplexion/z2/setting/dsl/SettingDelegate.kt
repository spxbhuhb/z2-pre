package hu.simplexion.z2.setting.dsl

import hu.simplexion.z2.auth.model.Principal
import hu.simplexion.z2.util.UUID
import kotlin.reflect.KProperty

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
actual class SettingDelegate<T>(
    actual val path: String,
    actual val owner: UUID<Principal>?,
    val encoder: (value: T) -> String,
    val decoder: (value: String?) -> T
) {

    actual var sensitive: Boolean = false
    actual var writable: Boolean = true
    var initialized : Boolean = false

    actual var valueOrNull: T? = null

    actual var value : T
        get() = requireNotNull(valueOrNull) { "missing setting: path=$path owner=$owner" }
        set(value) { valueOrNull = value }

    actual operator fun getValue(thisRef: Any?, property: KProperty<*>): T {
        return value
    }

    actual operator fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
        this.value = value
    }

    actual infix fun default(value : T) : SettingDelegate<T> {
        this.value = value
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