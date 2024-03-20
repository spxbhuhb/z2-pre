package hu.simplexion.z2.auth.impl

import hu.simplexion.z2.application.technicalAdminRole
import hu.simplexion.z2.auth.api.RoleApi
import hu.simplexion.z2.auth.context.*
import hu.simplexion.z2.auth.model.Grant
import hu.simplexion.z2.auth.model.Principal
import hu.simplexion.z2.auth.model.Role
import hu.simplexion.z2.auth.table.RoleGrantTable.Companion.roleGrantTable
import hu.simplexion.z2.auth.table.RoleGroupTable.Companion.roleGroupTable
import hu.simplexion.z2.auth.table.RoleTable.Companion.roleTable
import hu.simplexion.z2.baseStrings
import hu.simplexion.z2.history.util.securityHistory
import hu.simplexion.z2.localization.text.commonStrings
import hu.simplexion.z2.schematic.ensureValid
import hu.simplexion.z2.services.ServiceImpl
import hu.simplexion.z2.util.UUID

class RoleImpl : RoleApi, ServiceImpl<RoleImpl> {

    companion object {
        val roleImpl = RoleImpl().internal

        var addRoles = arrayOf(technicalAdminRole.uuid)
        var getRoles = arrayOf(technicalAdminRole.uuid)
        var updateRoles = arrayOf(technicalAdminRole.uuid)
    }

    override suspend fun query(): List<Role> {
        ensureAny(*getRoles)
        return roleTable.query()
    }

    override suspend fun add(role: Role) : UUID<Role> {
        ensureAny(*addRoles)
        ensureValid(role, true)

        val roleUuid = roleTable.insert(role)

        securityHistory(baseStrings.role, commonStrings.add, roleUuid, role)

        return roleUuid
    }

    override suspend fun update(role: Role) {
        ensureAll(*updateRoles)
        ensureValid(role)

        securityHistory(baseStrings.role, commonStrings.update, role.uuid, role)

        roleTable.update(role.uuid, role)
    }

    override suspend fun remove(uuid: UUID<Role>) {
        ensureAll(*updateRoles)
        securityHistory(baseStrings.role, commonStrings.remove, uuid)

        roleGrantTable.remove(uuid)
        roleTable.remove(uuid)
    }

    override suspend fun getByName(name: String): Role {
        ensureLoggedIn()
        return roleTable.getByName(name)
    }

    override suspend fun grant(role: UUID<Role>, principal: UUID<Principal>, context : String?) {
        ensureSecurityOfficer()
        securityHistory(baseStrings.role, baseStrings.grantRole, principal, role, context)

        roleGrantTable.insert(role, principal, context)
    }

    override suspend fun revoke(role: UUID<Role>, principal: UUID<Principal>, context : String?) {
        ensureSecurityOfficer()
        securityHistory(baseStrings.role, baseStrings.revokeRole, principal, role, context)

        roleGrantTable.remove(role, principal, context)
    }

    override suspend fun rolesOf(principal: UUID<Principal>, context: String?): List<Role> {
        ensureSelfOrSecurityOfficer(principal)
        return roleGrantTable.rolesOf(principal, context)
    }

    override suspend fun grantedTo(role: UUID<Role>, context : String?): List<Grant> {
        ensureSecurityOfficer()
        return roleGrantTable.grantedTo(role, context)
    }

    override suspend fun addToGroup(role: UUID<Role>, group: UUID<Role>) {
        ensureSecurityOfficer()
        roleGroupTable.add(role, group)
    }

    override suspend fun removeFromGroup(role: UUID<Role>, group: UUID<Role>) {
        ensureSecurityOfficer()
        roleGroupTable.remove(role, group)
    }

}