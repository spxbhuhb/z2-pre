package hu.simplexion.z2.setting.provider

import hu.simplexion.z2.auth.model.Principal
import hu.simplexion.z2.setting.model.Setting
import hu.simplexion.z2.util.UUID

/**
 * Uses a map to store and retrieve the settings. Intended use is browsers
 * where the actual settings are loaded from the server asynchronously.
 */
class InlineSettingProvider : SettingProvider {

    val items = mutableMapOf<UUID<Principal>?, MutableMap<String, String?>>()

    override var isReadOnly = false

    override fun put(owner: UUID<Principal>?, path: String, value: String?) {
        val ownerItems = items[owner] ?: mutableMapOf<String, String?>().also { items[owner] = it }
        ownerItems[path] = value
    }

    override fun get(owner: UUID<Principal>?, path: String, children: Boolean): List<Setting> {
        val ownerItems = items[owner] ?: mutableMapOf<String, String?>().also { items[owner] = it }

        if (!children) {
            return ownerItems[path]?.let { value ->
                listOf(
                    Setting().also {
                        it.path = path
                        it.value = value
                    }
                )
            } ?: emptyList()
        }

        val prefix = "$path/"

        return ownerItems.entries
            .filter { it.key.startsWith(path) || it.key.startsWith(prefix) }
            .map {
                Setting().apply {
                    this.path = it.key
                    this.value = it.value
                }
            }
    }
}