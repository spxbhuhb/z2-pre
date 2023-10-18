package hu.simplexion.z2.auth.table

import hu.simplexion.z2.auth.model.Credentials
import hu.simplexion.z2.auth.model.Principal
import hu.simplexion.z2.auth.table.PrincipalTable.Companion.principalTable
import hu.simplexion.z2.commons.util.UUID
import hu.simplexion.z2.exposed.SchematicUuidTable
import org.jetbrains.exposed.sql.SortOrder
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.kotlin.datetime.timestamp
import org.jetbrains.exposed.sql.select

open class CredentialsTable(
    principalTable: PrincipalTable
) : SchematicUuidTable<Credentials>(
    "z2_auth_credential",
    Credentials(),
) {

    companion object {
        val credentialsTable = CredentialsTable(principalTable)
    }

    val principal = reference("principal", principalTable)
    val type = varchar("type", 50)
    val value = text("value")
    val createdAt = timestamp("createdAt")

    fun readValue(inPrincipal: UUID<Principal>, inType: String): String? =
        slice(value)
            .select {
                (principal eq inPrincipal) and (type eq inType)
            }
            .orderBy(createdAt, SortOrder.DESC)
            .limit(1)
            .firstOrNull()
            ?.let { it[value] }

}