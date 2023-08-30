package hu.simplexion.z2.setting.util

import hu.simplexion.z2.auth.context.account
import hu.simplexion.z2.auth.util.runAsSecurityOfficer
import hu.simplexion.z2.commons.util.UUID
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

}