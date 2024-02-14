package hu.simplexion.z2.setting.dsl

import hu.simplexion.z2.auth.model.Principal
import hu.simplexion.z2.util.UUID
import kotlin.reflect.KProperty

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
expect class SettingDelegate<T> {

    val path: String
    val owner: UUID<Principal>?

    var sensitive: Boolean
    var writable: Boolean

    var valueOrNull: T?
    var value: T

    operator fun getValue(thisRef: Any?, property: KProperty<*>): T

    operator fun setValue(thisRef: Any?, property: KProperty<*>, value: T)

    infix fun default(value: T): SettingDelegate<T>

    operator fun rangeTo(settingFlag: SettingFlag): SettingDelegate<T>

}