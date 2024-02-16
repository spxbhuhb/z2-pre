package hu.simplexion.z2.setting.dsl

import hu.simplexion.z2.util.UUID
import kotlin.reflect.KClass

@Suppress("UNCHECKED_CAST")
actual fun <T : Any> setting(kClass: KClass<T>, path: String): SettingDelegate<T> =
    // FIXME add setting type support to the compiler plugin
    when (kClass) {
        Boolean::class -> SettingDelegate(path, null, { it.toString() }, { it !!.toBoolean() })
        Int::class -> SettingDelegate(path, null, { it.toString() }, { it !!.toInt() })
        String::class -> SettingDelegate(path, null, { it }, { it !! })
        UUID::class -> SettingDelegate(path, null, { it.toString() }, { UUID<Any>(it !!) })
        else -> throw NotImplementedError("not implemented setting class: $kClass")
    } as SettingDelegate<T>