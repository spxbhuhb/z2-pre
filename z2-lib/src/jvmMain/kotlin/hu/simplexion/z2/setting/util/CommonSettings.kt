package hu.simplexion.z2.setting.util

import hu.simplexion.z2.auth.context.principal
import hu.simplexion.z2.auth.util.runTransactionAsSecurityOfficer
import hu.simplexion.z2.schematic.Schematic
import hu.simplexion.z2.setting.provider.SqlSettingProvider.Companion.settingImpl
import hu.simplexion.z2.util.UUID

object CommonSettings {

    fun getDatabaseVersion(): String? {
        return runTransactionAsSecurityOfficer { context ->
            settingImpl(context).get(context.principal, DATABASE_VERSION_KEY)
        }.singleOrNull()?.value
    }

    fun setDatabaseVersion(value: String) {
        runTransactionAsSecurityOfficer { context ->
            settingImpl(context).put(context.principal, DATABASE_VERSION_KEY, value)
        }
    }

    fun <T : Schematic<T>> getSystemSettings(key: UUID<*>, schematic: T): T {
        runTransactionAsSecurityOfficer { context ->
            val basePath = key.toString()
            schematic.decodeFromSettings(basePath, settingImpl(context).get(context.principal, basePath, true))
        }
        return schematic
    }

    fun <T : Schematic<T>> putSystemSettings(key: UUID<*>, schematic: T) {
        runTransactionAsSecurityOfficer { context ->
            val basePath = key.toString()
            settingImpl(context).put(context.principal, schematic.encodeToSettings(basePath))
        }
    }

}