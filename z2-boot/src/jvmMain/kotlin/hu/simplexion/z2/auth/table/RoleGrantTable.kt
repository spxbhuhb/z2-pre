package hu.simplexion.z2.auth.table

import hu.simplexion.z2.auth.model.AccountPrivate
import hu.simplexion.z2.auth.model.AccountPublic
import hu.simplexion.z2.auth.model.Role
import hu.simplexion.z2.auth.table.AccountPrivateTable.Companion.accountPrivateTable
import hu.simplexion.z2.auth.table.RoleTable.Companion.roleTable
import hu.simplexion.z2.commons.util.UUID
import hu.simplexion.z2.exposed.jvm
import hu.simplexion.z2.exposed.z2
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq

open class RoleGrantTable(
    accountPrivateTable: AccountPrivateTable,
    roleTable: RoleTable
) : Table(
    "auth_role_grant"
) {

    companion object {
        val roleGrantTable = RoleGrantTable(accountPrivateTable, roleTable)
    }

    val account = reference("account", accountPrivateTable).index()
    val role = reference("role", roleTable)
    val context = varchar("context", 50).index().nullable()

    fun insert(inRole : UUID<Role>, inAccount: UUID<AccountPrivate>, inContext : String?) {
        insert {
            it[role] = inRole.jvm
            it[account] = inAccount.jvm
            it[context] = inContext
        }
    }

    fun remove(inRole : UUID<Role>) {
        deleteWhere { role eq inRole.jvm }
    }

    fun remove(inRole: UUID<Role>, inAccount: UUID<AccountPrivate>, inContext: String?) {
        deleteWhere {
            (role eq inRole.jvm) and (account eq inAccount.jvm) and (context eq inContext)
        }
    }

    fun rolesOf(inAccount: UUID<AccountPrivate>, inContext : String?) : List<Role> {

        val condition = if (inContext == null) {
            Op.build { account eq inAccount.jvm}
        } else {
            Op.build { (account eq inAccount.jvm) and (context eq inContext)}
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

    fun grantedTo(inRole: UUID<Role>, inContext: String?) : List<AccountPublic> {
        val condition = if (inContext == null) {
            Op.build { role eq inRole.jvm}
        } else {
            Op.build { (role eq inRole.jvm) and (context eq inContext)}
        }

        return this
            .join(
                accountPrivateTable,
                JoinType.INNER,
                additionalConstraint = { roleGrantTable.account eq accountPrivateTable.id }
            )
            .slice(
                roleGrantTable.account,
                accountPrivateTable.fullName
            )
            .select(condition)
            .map {
                AccountPublic().apply {
                    uuid = it[roleGrantTable.account].value.z2()
                    fullName = it[accountPrivateTable.fullName]
                }
            }
    }

}