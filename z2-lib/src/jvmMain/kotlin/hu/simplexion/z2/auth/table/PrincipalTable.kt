package hu.simplexion.z2.auth.table

import hu.simplexion.z2.auth.model.Principal
import hu.simplexion.z2.util.UUID
import hu.simplexion.z2.exposed.SchematicUuidTable
import org.jetbrains.exposed.sql.kotlin.datetime.timestamp
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.update

open class PrincipalTable : SchematicUuidTable<Principal>(
    "z2_auth_principal",
    Principal()
) {

    companion object {
        val principalTable = PrincipalTable()
    }

    val name = text("name").uniqueIndex()
    val activated = bool("activated")
    val locked = bool("locked")
    val expired = bool("expired")
    val anonymized = bool("anonymized")
    val lastAuthSuccess = timestamp("lastAuthSuccess").nullable()
    val authSuccessCount = integer("authSuccessCount")
    val lastAuthFail = timestamp("lastAuthFail").nullable()
    val authFailCount = integer("authFailCount")

    fun setLocked(inPrincipal : UUID<Principal>, inLocked : Boolean) {
        update({ id eq inPrincipal }) {
            it[locked] = inLocked
        }
    }

    fun setActivated(inPrincipal : UUID<Principal>, activated : Boolean) {
        update({ id eq inPrincipal }) {
            it[this.activated] = activated
        }
    }

    fun getByNameOrNull(inName : String) : Principal? =
        select { name eq inName }
            .map {
                it.toSchematic(this, newInstance())
            }
            .firstOrNull()

    fun updateName(inPrincipal : UUID<Principal>, inName : String) {
        update({ id eq inPrincipal }) {
            it[name] = inName
        }
    }
}