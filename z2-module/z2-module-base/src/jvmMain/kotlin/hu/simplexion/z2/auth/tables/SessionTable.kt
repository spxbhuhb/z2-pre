package hu.simplexion.z2.auth.tables

import hu.simplexion.z2.auth.model.Session
import hu.simplexion.z2.exposed.SchematicUuidTable
import org.jetbrains.exposed.sql.kotlin.datetime.timestamp

open class SessionTable : SchematicUuidTable<Session>(
    "auth_session",
    Session()
) {

    val account = uuid("account")
    val fullName = varchar("fullName", 100)
    val email = varchar("email", 264)
    val createdAt = timestamp("createdAt")

}