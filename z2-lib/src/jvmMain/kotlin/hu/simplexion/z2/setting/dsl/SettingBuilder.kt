package hu.simplexion.z2.setting.dsl

import hu.simplexion.z2.application.APPLICATION_UUID
import hu.simplexion.z2.auth.model.Principal
import hu.simplexion.z2.util.UUID

class SettingBuilder {
    var path : String = ""
    var owner : UUID<Principal> = APPLICATION_UUID
    var sensitive : Boolean = false

    fun toDelegate() = SettingDelegate(path, owner, sensitive, { it }, { it!! })

    infix fun Any.owner(principalId: UUID<Principal>): SettingBuilder {
        if (this is String) {
            path = this
        }
        this@SettingBuilder.owner = principalId
        return this@SettingBuilder
    }

    infix fun Any.sensitive(value: Boolean): SettingBuilder {
        if (this is String) {
            path = this
        }
        this@SettingBuilder.sensitive = value
        return this@SettingBuilder
    }
}