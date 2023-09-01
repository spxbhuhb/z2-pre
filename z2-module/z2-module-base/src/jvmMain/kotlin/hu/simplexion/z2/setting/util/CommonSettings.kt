package hu.simplexion.z2.setting.util

import hu.simplexion.z2.auth.context.account
import hu.simplexion.z2.auth.util.runAsSecurityOfficer
import hu.simplexion.z2.auth.util.runTransactionAsSecurityOfficer
import hu.simplexion.z2.commons.util.UUID
import hu.simplexion.z2.schematic.runtime.Schematic
import hu.simplexion.z2.setting.impl.SettingImpl.Companion.settingImpl
import kotlinx.coroutines.runBlocking
import org.jetbrains.exposed.sql.transactions.transaction

object CommonSettings {

    fun getDatabaseVersion(): String? {
        return runTransactionAsSecurityOfficer { context ->
            settingImpl(context).get(context.account, DATABASE_VERSION_KEY)
        }.singleOrNull()?.value
    }

    fun setDatabaseVersion(value: String) {
        runTransactionAsSecurityOfficer { context ->
            settingImpl(context).put(context.account, DATABASE_VERSION_KEY, value)
        }
    }

    fun <T : Schematic<T>> getSystemSettings(key: UUID<*>, schematic: T): T {
        runTransactionAsSecurityOfficer { context ->
            val basePath = key.toString()
            schematic.decodeFromSettings(basePath, settingImpl(context).get(context.account, basePath, true))
        }
        return schematic
    }

    fun <T : Schematic<T>> putSystemSettings(key: UUID<*>, schematic: T) {
        runTransactionAsSecurityOfficer { context ->
            val basePath = key.toString()
            settingImpl(context).put(context.account, schematic.encodeToSettings(basePath))
        }
    }

}