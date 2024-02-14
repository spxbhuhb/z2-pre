package hu.simplexion.z2.setting.dsl

import kotlin.reflect.KClass

actual fun <T:Any> setting(kClass : KClass<T>, path: String) : SettingDelegate<T> {
    TODO()
}
