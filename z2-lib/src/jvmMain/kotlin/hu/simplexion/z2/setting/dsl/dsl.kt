package hu.simplexion.z2.setting.dsl

@Suppress("UNCHECKED_CAST")
inline fun <reified T> setting(noinline builder: SettingBuilder.() -> Any?): SettingDelegate<T> =
    // FIXME add setting type support to the compiler plugin
    when {
        T::class == String::class -> stringSetting(builder) as SettingDelegate<T>
        else -> throw NotImplementedError("not implemented setting class: ${T::class}")
    }

fun stringSetting(builder: SettingBuilder.() -> Any?): SettingDelegate<String> =
    SettingBuilder().let {
        val returnValue = it.builder()
        // TODO add compiler plugin support for the setting builder
        when {
            it.path.isNotEmpty() -> Unit
            returnValue is String -> it.path = returnValue
            else -> throw IllegalStateException("setting path is not set")
        }
        it.toDelegate()
    }