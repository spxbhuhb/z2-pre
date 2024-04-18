package hu.simplexion.z2.setting.api

import hu.simplexion.z2.auth.model.Principal
import hu.simplexion.z2.services.Service
import hu.simplexion.z2.setting.model.Setting
import hu.simplexion.z2.util.UUID

interface SettingApi : Service {

    /**
     * Put the given setting into the setting database. Any previous values are
     * removed and any subsequent settings are removed.
     *
     * ```text
     * original:
     *   a/b/c  123
     *   a/bc   33
     *   a/b    12
     *
     * put("a/b", 45)
     *
     * result:
     *   a/bc   33
     *   a/b    45
     * ```
     *
     * @param  owner  The owner of the setting. Using different scope than the
     *                UUID of the caller requires `securityOfficer` role.
     */
    suspend fun put(owner : UUID<Principal>, path : String, value : String?)

    /**
     * Put the [settings] into the setting database.
     *
     * @param  owner  The owner of the setting. Using different scope than the
     *                UUID of the caller requires `securityOfficer` role.
     */
    suspend fun put(owner : UUID<Principal>, settings : List<Setting>)


//    /**
//     * Save all the non-default fields of [schematic] as an individual setting.
//     *
//     * @param  [basePath]  The path to put before any field name to get the setting path.
//     *                     For any given field the setting path is : `$basePath/${field.name}`
//     */
//    suspend fun <T : Schematic<T>> put(owner : UUID<Principal>, basePath : String, schematic : T) {
//        put(owner, schematic.encodeToSettings(basePath))
//    }

    /**
     * Get the settings that belongs to the [path].
     *
     * @param  scope       The owner of the setting. Using different owner than the
     *                     caller requires `securityOfficer` role.
     *
     * @param  children    Include all subsequent settings. For example, [path]
     *                     `a/b` returns with `a/b/c` as well. It does not return
     *                     with `a/bc`.
     */
    suspend fun get(owner: UUID<Principal>, path : String, children : Boolean = true) : List<Setting>

//    /**
//     * Set the fields of [schematic] from settings.
//     *
//     * @param  [basePath]  The path to put before any field name to get the setting path.
//     *                     For any given field the setting path is : `$basePath/${field.name}`
//     */
//    suspend fun <T : Schematic<T>> get(owner: UUID<Principal>, basePath: String, schematic : T) : T {
//        schematic.decodeFromSettings(basePath, get(owner, basePath, true))
//        return schematic
//    }

}