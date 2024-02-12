package hu.simplexion.z2.setting.util

import hu.simplexion.z2.setting.implementation.SettingImpl
import hu.simplexion.z2.setting.persistence.SettingTable
import hu.simplexion.z2.setting.provider.EnvironmentSettingProvider
import hu.simplexion.z2.setting.provider.PropertyFileSettingProvider
import hu.simplexion.z2.setting.provider.SqlSettingProvider
import java.nio.file.Paths

fun settings(builder : SettingProviderBuilder.() -> Unit) {
    SettingProviderBuilder().builder()
}

class SettingProviderBuilder {

    fun environment(prefix : () -> String) {
        SettingImpl.rootProvider += EnvironmentSettingProvider(prefix())
    }

    fun propertyFile(path : () -> String) {
        SettingImpl.rootProvider += PropertyFileSettingProvider(Paths.get(path()))
    }

    fun sql(table : () -> SettingTable) {
        SettingImpl.rootProvider += SqlSettingProvider(table())
    }

}