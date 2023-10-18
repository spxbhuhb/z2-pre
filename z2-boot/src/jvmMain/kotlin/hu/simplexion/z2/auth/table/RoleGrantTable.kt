package hu.simplexion.z2.auth.table

import hu.simplexion.z2.auth.model.Grant
import hu.simplexion.z2.auth.model.Principal
import hu.simplexion.z2.auth.model.Role
import hu.simplexion.z2.auth.table.PrincipalTable.Companion.principalTable
import hu.simplexion.z2.auth.table.RoleTable.Companion.roleTable
import hu.simplexion.z2.commons.util.UUID
import hu.simplexion.z2.exposed.jvm
import hu.simplexion.z2.exposed.z2
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq

open class RoleGrantTable(
    principalTable: PrincipalTable,
    roleTable: RoleTable
) : Table(
    "z2_auth_role_grant"
) {

    companion object {
        val roleGrantTable = RoleGrantTable(principalTable, roleTable)
    }

    val principal = reference("principal", principalTable).index()
    val role = reference("role", roleTable)
    val context = varchar("context", 50).index().nullable()

    fun insert(inRole: UUID<Role>, inPrincipal: UUID<Principal>, inContext: String?) {
        insert {
            it[role] = inRole.jvm
            it[principal] = inPrincipal.jvm
            it[context] = inContext
        }
    }

    fun remove(inRole: UUID<Role>) {
        deleteWhere { role eq inRole.jvm }
    }

    fun remove(inRole: UUID<Role>, inPrincipal: UUID<Principal>, inContext: String?) {
        deleteWhere {
            (role eq inRole.jvm) and (principal eq inPrincipal.jvm) and (context eq inContext)
        }
    }

    fun rolesOf(inPrincipal: UUID<Principal>, inContext: String?): List<Role> {

        val condition = if (inContext == null) {
            Op.build { principal eq inPrincipal.jvm }
        } else {
            Op.build { (principal eq inPrincipal.jvm) and (context eq inContext) }
        }

        return this
            .join(roleTable, JoinType.INNER, additionalConstraint = { roleGrantTable.role eq roleTable.id })
            .slice(
                roleGrantTable.role,
                roleTable.contextName,
                roleTable.programmaticName,
                roleTable.displayName
            )
            .select(condition)
            .map {
                Role().apply {// FIXME schematic init pattern
                    uuid = it[role].value.z2()
                    contextName = it[roleTable.contextName]
                    programmaticName = it[roleTable.programmaticName]
                    displayName = it[roleTable.displayName]
                }
            }
    }

    fun grantedTo(inRole: UUID<Role>, inContext: String?): List<Grant> {
        val condition = if (inContext == null) {
            Op.build { role eq inRole.jvm }
        } else {
            Op.build { (role eq inRole.jvm) and (context eq inContext) }
        }

        return this
            .join(
                principalTable,
                JoinType.INNER,
                additionalConstraint = { roleGrantTable.principal eq principalTable.id }
            )
            .slice(
                roleGrantTable.principal,
                principalTable.name
            )
            .select(condition)
            .map { row ->
                Grant().also {
                    it.role = inRole
                    it.principal = row[roleGrantTable.principal].value.z2()
                    it.principalName = row[principalTable.name]
                }
            }
    }
}