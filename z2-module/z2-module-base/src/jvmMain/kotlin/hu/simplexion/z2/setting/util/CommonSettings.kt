package hu.simplexion.z2.setting.util

import hu.simplexion.z2.auth.context.account
import hu.simplexion.z2.auth.util.runAsSecurityOfficer
import hu.simplexion.z2.commons.util.UUID
import hu.simplexion.z2.schematic.runtime.Schematic
import hu.simplexion.z2.setting.impl.SettingImpl.Companion.settingImpl
import kotlinx.coroutines.runBlocking
import org.jetbrains.exposed.sql.transactions.transaction

object CommonSettings {

    fun getDatabaseVersion(): String? {
        return runAsSecurityOfficer { context ->
            transaction {
                runBlocking {
                    settingImpl(context).get(context.account, DATABASE_VERSION_KEY)
                }
            }
        }.singleOrNull()?.value
    }

    fun setDatabaseVersion(value: String) {
        runAsSecurityOfficer { context ->
            transaction {
                runBlocking {
                    settingImpl(context).put(context.account, DATABASE_VERSION_KEY, value)
                }
            }
        }
    }

    fun <T : Schematic<T>> getSystemSettings(key : UUID<*>, schematic: T) : T {
        runAsSecurityOfficer { context ->
            transaction {
                runBlocking {
                    val basePath = key.toString()
                    schematic.decodeFromSettings(basePath, settingImpl(context).get(context.account, basePath, true))
                }
            }
        }
        return schematic
    }

    fun <T : Schematic<T>> putSystemSettings(key : UUID<*>, schematic: T) {
        runAsSecurityOfficer { context ->
            transaction {
                runBlocking {
                    val basePath = key.toString()
                    settingImpl(context).put(context.account, schematic.encodeToSettings(basePath))
                }
            }
        }
    }

}