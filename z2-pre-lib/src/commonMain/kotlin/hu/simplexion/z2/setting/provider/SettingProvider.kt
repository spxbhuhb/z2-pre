package hu.simplexion.z2.setting.provider

import hu.simplexion.z2.auth.model.Principal
import hu.simplexion.z2.setting.model.Setting
import hu.simplexion.z2.util.UUID

interface SettingProvider {

    val isReadOnly : Boolean

    fun put(owner : UUID<Principal>?, path : String, value : String?)

    fun get(owner: UUID<Principal>?, path : String, children : Boolean = true) : List<Setting>

}