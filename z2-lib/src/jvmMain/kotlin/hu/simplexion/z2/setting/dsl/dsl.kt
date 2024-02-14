package hu.simplexion.z2.setting.dsl

import hu.simplexion.z2.application.APPLICATION_UUID
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

// ---------------------------------------------------------------------
// Setting value builders
// ---------------------------------------------------------------------

@Suppress("UNCHECKED_CAST")
inline fun <reified T> setting(noinline path: () -> String): SettingDelegate<T> =
    // FIXME add setting type support to the compiler plugin
    when (T::class) {
        Boolean::class -> SettingDelegate(path(), APPLICATION_UUID, { it.toString() }, { it !!.toBoolean() })
        Int::class -> SettingDelegate(path(), APPLICATION_UUID, { it.toString() }, { it !!.toInt() })
        Path::class -> SettingDelegate(path(), APPLICATION_UUID, { it.toString() }, { Paths.get(it) })
        String::class -> SettingDelegate(path(), APPLICATION_UUID, { it }, { it !! })

        else -> throw NotImplementedError("not implemented setting class: ${T::class}")
    } as SettingDelegate<T>

// ---------------------------------------------------------------------
// Setting provider builders
// ---------------------------------------------------------------------

fun settings(builder: SettingProviderBuilder.() -> Unit) {
    SettingProviderBuilder().builder()
}

class SettingProviderBuilder {

    fun environment(owner: UUID<Principal> = APPLICATION_UUID, prefix: () -> String) {
        SettingImpl.rootProvider += EnvironmentSettingProvider(prefix(), owner)
    }

    fun propertyFile(owner: UUID<Principal> = APPLICATION_UUID, optional: Boolean = true, path: () -> String) {
        SettingImpl.rootProvider += PropertyFileSettingProvider(Paths.get(path()), optional, owner)
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