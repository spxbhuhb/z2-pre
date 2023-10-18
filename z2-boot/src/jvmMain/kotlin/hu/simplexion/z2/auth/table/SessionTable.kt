package hu.simplexion.z2.auth.table

import hu.simplexion.z2.auth.model.Session
import hu.simplexion.z2.exposed.SchematicUuidTable
import org.jetbrains.exposed.sql.kotlin.datetime.timestamp

open class SessionTable : SchematicUuidTable<Session>(
    "z2_auth_session",
    Session()
) {

    companion object {
        val sessionTable = SessionTable()
    }

    val principal = uuid("principal")
    val createdAt = timestamp("createdAt")

}