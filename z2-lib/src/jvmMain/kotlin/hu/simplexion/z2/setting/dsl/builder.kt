package hu.simplexion.z2.setting.dsl

import hu.simplexion.z2.application.APPLICATION_UUID
import hu.simplexion.z2.auth.model.Principal
import hu.simplexion.z2.setting.implementation.SettingImpl
import hu.simplexion.z2.setting.persistence.SettingTable
import hu.simplexion.z2.setting.provider.EnvironmentSettingProvider
import hu.simplexion.z2.setting.provider.PropertyFileSettingProvider
import hu.simplexion.z2.setting.provider.SqlSettingProvider
import hu.simplexion.z2.util.UUID
import java.nio.file.Paths

fun settings(builder : SettingProviderBuilder.() -> Unit) {
    SettingProviderBuilder().builder()
}

class SettingProviderBuilder {

    fun environment(owner : UUID<Principal> = APPLICATION_UUID, prefix : () -> String) {
        SettingImpl.rootProvider += EnvironmentSettingProvider(prefix(), owner)
    }

    fun propertyFile(owner : UUID<Principal> = APPLICATION_UUID, path : () -> String) {
        SettingImpl.rootProvider += PropertyFileSettingProvider(Paths.get(path()), owner)
    }

    fun sql(table : () -> SettingTable) {
        SettingImpl.rootProvider += SqlSettingProvider(table())
    }

}