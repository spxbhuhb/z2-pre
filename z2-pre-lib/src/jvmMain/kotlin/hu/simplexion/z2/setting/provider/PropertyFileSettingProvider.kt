package hu.simplexion.z2.setting.provider

import hu.simplexion.z2.application.applicationSettings
import hu.simplexion.z2.auth.context.ensureFailNotImplemented
import hu.simplexion.z2.auth.model.Principal
import hu.simplexion.z2.setting.model.Setting
import hu.simplexion.z2.util.UUID
import java.io.InputStreamReader
import java.nio.charset.StandardCharsets
import java.nio.file.Path
import java.util.*
import kotlin.io.path.exists
import kotlin.io.path.inputStream

/**
 * Get settings from a property file. Supports only [get], [put] throws an
 * exception. Must be security officer or internal call.
 *
 * @property  path  The file system path to the property file.
 * @property  optional  When true a missing file is treated as empty
 * @property  owner  The owner of the settings in this property file.
 */
class PropertyFileSettingProvider(
    val path: Path,
    val optional: Boolean
) : SettingProvider {

    override val isReadOnly: Boolean
        get() = true

    val prop = Properties()

    init {
        prop.clear()
        if (path.exists() || ! optional) {
            path.inputStream().use {
                prop.load(InputStreamReader(it, StandardCharsets.UTF_8))
            }
        }
    }

    override fun put(owner: UUID<Principal>?, path: String, value: String?) {
        ensureFailNotImplemented { "property file settings are read-only" }
    }

    override fun get(owner: UUID<Principal>?, path: String, children: Boolean): List<Setting> {
        if (owner != null && owner != applicationSettings.applicationUuid) return emptyList()

        val value = prop.getProperty(path)

        return if (value != null) {
            listOf(Setting().also { it.path = path; it.value = value })
        } else {
            emptyList()
        }
    }

}