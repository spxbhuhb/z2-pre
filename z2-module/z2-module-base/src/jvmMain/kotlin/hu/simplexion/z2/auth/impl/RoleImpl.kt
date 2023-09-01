package hu.simplexion.z2.auth.impl

import hu.simplexion.z2.auth.api.RoleApi
import hu.simplexion.z2.auth.context.ensure
import hu.simplexion.z2.auth.context.has
import hu.simplexion.z2.auth.context.isAccount
import hu.simplexion.z2.auth.model.AccountPrivate
import hu.simplexion.z2.auth.model.AccountPublic
import hu.simplexion.z2.auth.model.Role
import hu.simplexion.z2.auth.securityOfficerRole
import hu.simplexion.z2.auth.table.RoleGrantTable.Companion.roleGrantTable
import hu.simplexion.z2.auth.table.RoleTable.Companion.roleTable
import hu.simplexion.z2.auth.ui.authStrings
import hu.simplexion.z2.commons.i18n.commonStrings
import hu.simplexion.z2.commons.util.UUID
import hu.simplexion.z2.history.util.securityHistory
import hu.simplexion.z2.schematic.runtime.dump
import hu.simplexion.z2.schematic.runtime.validate
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
        // FIXME uuid validation validate(role)

        val roleUuid = roleTable.insert(role)

        securityHistory(authStrings.role, commonStrings.add, roleUuid, role.dump())

        return roleUuid
    }

    override suspend fun update(role: Role) {
        ensure(securityOfficerRole)
        validate(role)

        securityHistory(authStrings.role, commonStrings.update, role.uuid, role.dump())

        roleTable.update(role.uuid, role)
    }

    override suspend fun remove(uuid: UUID<Role>) {
        ensure(securityOfficerRole)
        securityHistory(authStrings.role, commonStrings.remove, uuid)

        roleGrantTable.remove(uuid)
        roleTable.remove(uuid)
    }

    override suspend fun grant(role: UUID<Role>, account: UUID<AccountPrivate>, context : String?) {
        ensure(securityOfficerRole)
        securityHistory(authStrings.role, authStrings.grantRole, account, role, context)

        roleGrantTable.insert(role, account, context)
    }

    override suspend fun revoke(role: UUID<Role>, account: UUID<AccountPrivate>, context : String?) {
        ensure(securityOfficerRole)
        securityHistory(authStrings.role, authStrings.revokeRole, account, role, context)

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

}