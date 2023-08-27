package hu.simplexion.z2.setting.impl

import hu.simplexion.z2.auth.context.ensureSelfOrSecurityOfficer
import hu.simplexion.z2.auth.model.AccountPrivate
import hu.simplexion.z2.commons.util.UUID
import hu.simplexion.z2.history.util.settingHistory
import hu.simplexion.z2.service.runtime.ServiceImpl
import hu.simplexion.z2.setting.api.SettingApi
import hu.simplexion.z2.setting.model.Setting
import hu.simplexion.z2.setting.table.SettingTable.Companion.settingTable

class SettingImpl : SettingApi, ServiceImpl<SettingImpl> {

    companion object {
        val settingImpl = SettingImpl()
    }

    override suspend fun put(owner: UUID<AccountPrivate>, path: String, value: String) {
        ensureSelfOrSecurityOfficer(owner)
        settingHistory(owner, path, value)
        settingTable.put(owner, path, value)
    }

    override suspend fun put(owner: UUID<AccountPrivate>, settings: List<Setting>) {
        ensureSelfOrSecurityOfficer(owner)
        for (setting in settings) {
            settingHistory(owner, setting.path, setting.value)
            settingTable.put(owner, setting.path, setting.value) // TODO put all settings into one insert
        }
    }

    override suspend fun get(owner: UUID<AccountPrivate>, path: String, children: Boolean): List<Setting> {
        ensureSelfOrSecurityOfficer(owner)
        return settingTable.get(owner, path, children)
    }

}