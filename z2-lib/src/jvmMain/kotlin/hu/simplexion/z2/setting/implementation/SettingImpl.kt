package hu.simplexion.z2.setting.implementation

import hu.simplexion.z2.auth.context.ensureInternal
import hu.simplexion.z2.auth.context.ensureSecurityOfficerOrInternal
import hu.simplexion.z2.auth.context.ensureSelfOrSecurityOfficer
import hu.simplexion.z2.auth.model.Principal
import hu.simplexion.z2.baseStrings
import hu.simplexion.z2.history.util.settingHistory
import hu.simplexion.z2.localization.text.commonStrings
import hu.simplexion.z2.services.ServiceImpl
import hu.simplexion.z2.setting.api.SettingApi
import hu.simplexion.z2.setting.implementation.SettingImpl.Companion.rootProvider
import hu.simplexion.z2.setting.model.Setting
import hu.simplexion.z2.setting.provider.DelegatingSettingProvider
import hu.simplexion.z2.util.UUID

/**
 * Uses [rootProvider] to read/write settings.
 */
class SettingImpl : SettingApi, ServiceImpl<SettingImpl> {

    companion object {
        val rootProvider = DelegatingSettingProvider()
    }

    override suspend fun put(owner: UUID<Principal>, path: String, value: String?) {
        ensureSelfOrSecurityOfficer(owner)

        settingHistory(owner, baseStrings.settings, commonStrings.update, path to value)
    }

    override suspend fun put(owner: UUID<Principal>, settings: List<Setting>) {
        ensureSelfOrSecurityOfficer(owner)

        for (setting in settings) {
            rootProvider.put(owner, setting.path, setting.value)
            settingHistory(owner, baseStrings.settings, commonStrings.update, setting.path to setting.value)
        }
    }

    override suspend fun get(owner: UUID<Principal>, path: String, children: Boolean): List<Setting> {
        ensureSelfOrSecurityOfficer(owner)

        return rootProvider.get(owner, path, children)
    }

}