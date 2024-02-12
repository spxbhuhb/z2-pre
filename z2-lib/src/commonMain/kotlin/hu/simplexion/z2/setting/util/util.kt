package hu.simplexion.z2.setting.util

import hu.simplexion.z2.auth.model.Principal
import hu.simplexion.z2.schematic.Schematic
import hu.simplexion.z2.setting.api.SettingApi
import hu.simplexion.z2.setting.model.Setting
import hu.simplexion.z2.util.UUID

/**
 * Set the fields of [schematic] from settings.
 *
 * @param  [basePath]  The path to put before any field name to get the setting path.
 *                     For any given field the setting path is : `$basePath/${field.name}`
 */
suspend fun <T : Schematic<T>> SettingApi.get(owner: UUID<Principal>, basePath: String, schematic : T) : T {
    schematic.decodeFromSettings(basePath, get(owner, basePath, true))
    return schematic
}

/**
 * Decodes the values from [settings] into this schematic. Sets any values not present
 * in [settings] to their schematic default.
 *
 * @param  [basePath]  The path to put before any field name to get the setting path.
 *                     For any given field the setting path is : `$basePath/${field.name}`
 */
fun <T : Schematic<T>> T.decodeFromSettings(basePath: String, settings: List<Setting>) {
    for (field in schematicSchema.fields) {
        if (field.name == "uuid") continue
        val settingsValue = settings.firstOrNull { it.path == "$basePath/${field.name}" }?.value
        val actualValue = if (settingsValue != null || field.isNullable) {
            field.toTypedValue(settingsValue, mutableListOf()) // FIXME fails are ignored
        } else {
            if (field.isNullable) field.definitionDefault else field.definitionDefault ?: field.naturalDefault
        }
        schematicCompanion.setFieldValue(this, field.name, actualValue)
    }
}

/**
 * Save all the non-default fields of [schematic] as an individual setting.
 *
 * @param  [basePath]  The path to put before any field name to get the setting path.
 *                     For any given field the setting path is : `$basePath/${field.name}`
 */
suspend fun <T : Schematic<T>> SettingApi.put(owner : UUID<Principal>, basePath : String, schematic : T) {
    put(owner, schematic.encodeToSettings(basePath))
}

/**
 * Encode the schematic into a list of settings.
 *
 * As of now, this function uses a simple `value.toString()`, so it is not
 * suitable for non-primitive data types.
 *
 * @param  basePath  The string to put before the field names to construct the
 *                   setting path. The path for any given field is constructed
 *                   with: `$basePath/${field.name}`.
 *
 */
fun <T : Schematic<T>> T.encodeToSettings(basePath: String): List<Setting> {
    val result = mutableListOf<Setting>()
    for (field in schematicSchema.fields) {
        if (field.name == "uuid") continue
        val value = schematicCompanion.getFieldValue(this, field.name)
        // this causes trouble when trying to set back to default value
        // if (field.definitionDefault == value) continue
        result += Setting().also {
            it.path = "$basePath/${field.name}"
            it.value = field.encodeToString(this)
        }
    }
    return result
}