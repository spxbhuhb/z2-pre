package hu.simplexion.z2.setting.provider

import hu.simplexion.z2.auth.context.ensureFailNotImplemented
import hu.simplexion.z2.auth.model.Principal
import hu.simplexion.z2.setting.model.Setting
import hu.simplexion.z2.util.UUID
import java.io.InputStreamReader
import java.nio.charset.StandardCharsets
import java.nio.file.Path
import java.util.*
import kotlin.io.path.inputStream

/**
 * Get settings from a property file. Supports only [get], [put] throws an
 * exception. Must be security officer or internal call.
 */
class PropertyFileSettingProvider(
    val path: Path
) : SettingProvider {

    override val isReadOnly: Boolean
        get() = true

    val prop = Properties()

    init {
        prop.clear()
        path.inputStream().use {
            prop.load(InputStreamReader(it, StandardCharsets.UTF_8))
        }
    }

    override fun put(owner: UUID<Principal>, path: String, value: String?) {
        ensureFailNotImplemented { "property file settings are read-only" }
    }

    override fun get(owner: UUID<Principal>, path: String, children: Boolean): List<Setting> {
        val value = prop.getProperty(path)

        return if (value != null) {
            listOf(Setting().also { it.path = path; it.value = value })
        } else {
            emptyList()
        }
    }

}