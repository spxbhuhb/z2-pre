package hu.simplexion.z2.auth.impl

import hu.simplexion.z2.auth.api.RoleApi
import hu.simplexion.z2.auth.context.ensure
import hu.simplexion.z2.auth.context.has
import hu.simplexion.z2.auth.context.isAccount
import hu.simplexion.z2.auth.model.AccountPrivate
import hu.simplexion.z2.auth.model.AccountPublic
import hu.simplexion.z2.auth.model.Role
import hu.simplexion.z2.auth.roleGrantTable
import hu.simplexion.z2.auth.roleTable
import hu.simplexion.z2.auth.securityOfficerRole
import hu.simplexion.z2.auth.ui.strings
import hu.simplexion.z2.commons.util.UUID
import hu.simplexion.z2.history.securityHistory
import hu.simplexion.z2.schematic.runtime.dump
import hu.simplexion.z2.service.runtime.ServiceImpl

class RoleImpl : RoleApi, ServiceImpl {

    override suspend fun list(): List<Role> {
        ensure(securityOfficerRole)
        return roleTable.list()
    }

    override suspend fun add(role: Role) : UUID<Role> {
        ensure(securityOfficerRole)
        securityHistory(strings.addRole, role.dump())

        return roleTable.insert(role)
    }

    override suspend fun update(role: Role) {
        ensure(securityOfficerRole)
        securityHistory(strings.editRole, role.dump())

        roleTable.update(role.id, role)
    }

    override suspend fun remove(uuid: UUID<Role>) {
        ensure(securityOfficerRole)
        securityHistory(strings.removeRole, uuid)

        roleGrantTable.remove(uuid)
        roleTable.remove(uuid)
    }

    override suspend fun grant(role: UUID<Role>, account: UUID<AccountPrivate>, context : UUID<Any>) {
        ensure(securityOfficerRole)
        securityHistory(strings.grantRole, role, account, context)

        roleGrantTable.insert(role, account, context)
    }

    override suspend fun revoke(role: UUID<Role>, account: UUID<AccountPrivate>, context : UUID<Any>) {
        ensure(securityOfficerRole)
        securityHistory(strings.revokeRole, role, account, context)

        roleGrantTable.remove(role, account, context)
    }

    override suspend fun rolesOf(account: UUID<AccountPrivate>, context: UUID<Any>): List<Role> {
        ensure(serviceContext.has(securityOfficerRole) or serviceContext.isAccount(account))
        return roleGrantTable.rolesOf(account, context)
    }

    override suspend fun grantedTo(role: UUID<Role>, context : UUID<Any>): List<AccountPublic> {
        ensure(securityOfficerRole)
        return roleGrantTable.grantedTo(role, context)
    }

}