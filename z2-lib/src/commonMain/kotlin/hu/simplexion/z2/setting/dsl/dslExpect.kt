package hu.simplexion.z2.setting.dsl

import kotlin.reflect.KClass

inline fun <reified T : Any> setting(noinline path: () -> String): SettingDelegate<T> =
    setting(T::class, path())

expect fun <T : Any> setting(kClass: KClass<T>, path: String): SettingDelegate<T>
