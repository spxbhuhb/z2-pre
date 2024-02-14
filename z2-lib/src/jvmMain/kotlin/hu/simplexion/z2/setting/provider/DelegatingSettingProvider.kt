package hu.simplexion.z2.setting.provider

import hu.simplexion.z2.auth.model.Principal
import hu.simplexion.z2.setting.model.Setting
import hu.simplexion.z2.setting.persistence.SettingTable.Companion.settingTable
import hu.simplexion.z2.util.Lock
import hu.simplexion.z2.util.UUID
import hu.simplexion.z2.util.use

/**
 * Delegates setting operations to other providers
 */
class DelegatingSettingProvider : SettingProvider {

    private val lock = Lock()

    private var providers = emptyList<SettingProvider>()

    override var isReadOnly: Boolean = true

    /**
     * Add a setting provider. The last provider added is called first for all
     * API calls.
     */
    operator fun plusAssign(provider: SettingProvider) {
        lock.use {
            providers = listOf(provider) + providers
            if (!provider.isReadOnly) isReadOnly = false
        }
    }

    override fun put(owner: UUID<Principal>?, path: String, value: String?) {
        check(!isReadOnly) { "settings in this application are read-only" }

        for (provider in lock.use { providers }) {
            if (provider.isReadOnly) continue
            provider.put(owner, path, value)
            return
        }
    }

    override fun get(owner: UUID<Principal>?, path: String, children: Boolean): List<Setting> {
        for (provider in lock.use { providers }) {
            val result = provider.get(owner, path, children)
            if (result.isNotEmpty()) return result
        }
        return emptyList()
    }
}