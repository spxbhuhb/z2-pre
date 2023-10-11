package hu.simplexion.z2.auth.impl

import hu.simplexion.z2.auth.api.RoleApi
import hu.simplexion.z2.auth.context.ensure
import hu.simplexion.z2.auth.context.ensureLoggedIn
import hu.simplexion.z2.auth.context.has
import hu.simplexion.z2.auth.context.isAccount
import hu.simplexion.z2.auth.model.AccountPrivate
import hu.simplexion.z2.auth.model.AccountPublic
import hu.simplexion.z2.auth.model.Role
import hu.simplexion.z2.auth.securityOfficerRole
import hu.simplexion.z2.auth.table.RoleGrantTable.Companion.roleGrantTable
import hu.simplexion.z2.auth.table.RoleGroupTable.Companion.roleGroupTable
import hu.simplexion.z2.auth.table.RoleTable.Companion.roleTable
import hu.simplexion.z2.baseStrings
import hu.simplexion.z2.commons.localization.text.commonStrings
import hu.simplexion.z2.commons.util.UUID
import hu.simplexion.z2.history.util.securityHistory
import hu.simplexion.z2.schematic.runtime.ensureValid
import hu.simplexion.z2.service.runtime.ServiceImpl

class RoleImpl : RoleApi, ServiceImpl<RoleImpl> {

    companion object {
        val roleImpl = RoleImpl()
    }

    override suspend fun list(): List<Role> {
        ensure(securityOfficerRole)
        return roleTable.list()
    }

    override suspend fun add(role: Role) : UUID<Role> {
        ensure(securityOfficerRole)
        ensureValid(role, true)

        val roleUuid = roleTable.insert(role)

        securityHistory(baseStrings.role, commonStrings.add, roleUuid, role)

        return roleUuid
    }

    override suspend fun update(role: Role) {
        ensure(securityOfficerRole)
        ensureValid(role)

        securityHistory(baseStrings.role, commonStrings.update, role.uuid, role)

        roleTable.update(role.uuid, role)
    }

    override suspend fun remove(uuid: UUID<Role>) {
        ensure(securityOfficerRole)
        securityHistory(baseStrings.role, commonStrings.remove, uuid)

        roleGrantTable.remove(uuid)
        roleTable.remove(uuid)
    }

    override suspend fun getByName(name: String): Role {
        ensureLoggedIn()
        return roleTable.getByName(name)
    }

    override suspend fun grant(role: UUID<Role>, account: UUID<AccountPrivate>, context : String?) {
        ensure(securityOfficerRole)
        securityHistory(baseStrings.role, baseStrings.grantRole, account, role, context)

        roleGrantTable.insert(role, account, context)
    }

    override suspend fun revoke(role: UUID<Role>, account: UUID<AccountPrivate>, context : String?) {
        ensure(securityOfficerRole)
        securityHistory(baseStrings.role, baseStrings.revokeRole, account, role, context)

        roleGrantTable.remove(role, account, context)
    }

    override suspend fun rolesOf(account: UUID<AccountPrivate>, context: String?): List<Role> {
        ensure(serviceContext.has(securityOfficerRole) or serviceContext.isAccount(account))
        return roleGrantTable.rolesOf(account, context)
    }

    override suspend fun grantedTo(role: UUID<Role>, context : String?): List<AccountPublic> {
        ensure(securityOfficerRole)
        return roleGrantTable.grantedTo(role, context)
    }

    override suspend fun addToGroup(role: UUID<Role>, group: UUID<Role>) {
        roleGroupTable.add(role, group)
    }

    override suspend fun removeFromGroup(role: UUID<Role>, group: UUID<Role>) {
        roleGroupTable.remove(role, group)
    }

}