package hu.simplexion.z2.setting.dsl

import hu.simplexion.z2.application.ApplicationSettings.applicationUuid
import hu.simplexion.z2.auth.model.Principal
import hu.simplexion.z2.exposed.tables
import hu.simplexion.z2.setting.implementation.SettingImpl
import hu.simplexion.z2.setting.persistence.SettingTable
import hu.simplexion.z2.setting.provider.EnvironmentSettingProvider
import hu.simplexion.z2.setting.provider.PropertyFileSettingProvider
import hu.simplexion.z2.setting.provider.SqlSettingProvider
import hu.simplexion.z2.util.UUID
import java.nio.file.Path
import java.nio.file.Paths
import kotlin.reflect.KClass

// ---------------------------------------------------------------------
// Setting value builders
// ---------------------------------------------------------------------


@Suppress("UNCHECKED_CAST")
actual fun <T : Any> setting(kClass: KClass<T>, path: String): SettingDelegate<T> =
    // FIXME add setting type support to the compiler plugin
    when (kClass) {
        Boolean::class -> SettingDelegate(path, null, { it.toString() }, { it!!.toBoolean() })
        Int::class -> SettingDelegate(path, null, { it.toString() }, { it!!.toInt() })
        Path::class -> SettingDelegate(path, null, { it.toString() }, { Paths.get(it!!) })
        String::class -> SettingDelegate(path, null, { it }, { it!! })
        UUID::class -> SettingDelegate(path, null, { it.toString() }, { UUID<Any>(it!!) })
        else -> throw NotImplementedError("not implemented setting class: $kClass")
    } as SettingDelegate<T>

// ---------------------------------------------------------------------
// Setting provider builders
// ---------------------------------------------------------------------

fun settings(builder: SettingProviderBuilder.() -> Unit) {
    SettingProviderBuilder().builder()
}

class SettingProviderBuilder {

    fun environment(prefix: () -> String) {
        SettingImpl.rootProvider += EnvironmentSettingProvider(prefix())
    }

    fun propertyFile(optional: Boolean = true, path: () -> String) {
        SettingImpl.rootProvider += PropertyFileSettingProvider(Paths.get(path()), optional)
    }

    /**
     * Add an [SqlSettingProvider] that uses the table returned by [table].
     * Calls [tables] to create and/or update the table if necessary.
     */
    fun sql(table: () -> SettingTable) {
        table().also {
            tables(it)
            SettingImpl.rootProvider += SqlSettingProvider(it)
        }
    }

}