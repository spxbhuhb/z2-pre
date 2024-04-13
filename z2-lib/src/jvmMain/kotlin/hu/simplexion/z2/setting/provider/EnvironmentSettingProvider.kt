package hu.simplexion.z2.setting.provider

import hu.simplexion.z2.application.applicationSettings
import hu.simplexion.z2.auth.context.ensureFailNotImplemented
import hu.simplexion.z2.auth.model.Principal
import hu.simplexion.z2.setting.model.Setting
import hu.simplexion.z2.util.UUID

/**
 * Get settings from environment variables. Supports only [get], [put] throws an
 * exception. Must be security officer or internal call.
 *
 * @property environmentVariablePrefix A string to add to property names as a prefix. Default is 'Z2_' which
 *                                     means that each name is prefixed like `Z2_SOME_NAME`. Set to empty string
 *                                     to disable use any prefixing.
 * @property  owner  The owner of the settings stored in the environment variables with the given prefix.
 */
class EnvironmentSettingProvider(
    val environmentVariablePrefix : String
) : SettingProvider {

    override val isReadOnly: Boolean
        get() = true

    override fun put(owner: UUID<Principal>?, path: String, value: String?) {
        ensureFailNotImplemented { "environment variable settings are read-only" }
    }

    override fun get(owner: UUID<Principal>?, path: String, children: Boolean): List<Setting> {
        if (owner != null && owner != applicationSettings.applicationUuid) return emptyList()

        val value = System.getenv("$environmentVariablePrefix$this")
        return if (value == null) {
            emptyList()
        } else listOf(Setting().also {
            it.path = path
            it.value = value
        })
    }

}