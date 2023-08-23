package hu.simplexion.z2.auth.tables

import hu.simplexion.z2.auth.accountPrivateTable
import hu.simplexion.z2.auth.model.AccountPrivate
import hu.simplexion.z2.auth.model.AccountPublic
import hu.simplexion.z2.auth.model.Role
import hu.simplexion.z2.auth.roleGrantTable
import hu.simplexion.z2.auth.roleTable
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

    val account = reference("account", accountPrivateTable).index()
    val role = reference("role", roleTable)
    val context = uuid("context").index()

    fun insert(inRole : UUID<Role>, inAccount: UUID<AccountPrivate>, inContext : UUID<Any>) {
        insert {
            it[role] = inRole.jvm
            it[account] = inAccount.jvm
            it[context] = inContext.jvm
        }
    }

    fun remove(inRole : UUID<Role>) {
        deleteWhere { role eq inRole.jvm }
    }

    fun remove(inRole: UUID<Role>, inAccount: UUID<AccountPrivate>, inContext: UUID<Any>) {
        deleteWhere {
            (role eq inRole.jvm) and (account eq inAccount.jvm) and (context eq inContext.jvm)
        }
    }

    fun rolesOf(inAccount: UUID<AccountPrivate>, inContext : UUID<Any>?) : List<Role> {

        val condition = if (inContext == null) {
            Op.build { account eq inAccount.jvm}
        } else {
            Op.build { (account eq inAccount.jvm) and (context eq inContext.jvm)}
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
                    id = it[role].value.z2()
                    contextName = it[roleTable.contextName]
                    programmaticName = it[roleTable.programmaticName]
                    displayName = it[roleTable.displayName]
                }
            }
    }

    fun grantedTo(inRole: UUID<Role>, inContext: UUID<Any>?) : List<AccountPublic> {
        val condition = if (inContext == null) {
            Op.build { role eq inRole.jvm}
        } else {
            Op.build { (role eq inRole.jvm) and (context eq inContext.jvm)}
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
                    id = it[roleGrantTable.account].value.z2()
                    fullName = it[accountPrivateTable.fullName]
                }
            }
    }

}