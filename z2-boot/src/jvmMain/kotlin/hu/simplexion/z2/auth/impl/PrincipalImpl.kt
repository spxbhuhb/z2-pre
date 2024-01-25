package hu.simplexion.z2.auth.impl

import hu.simplexion.z2.auth.api.PrincipalApi
import hu.simplexion.z2.auth.context.*
import hu.simplexion.z2.auth.impl.AuthAdminImpl.Companion.authAdminImpl
import hu.simplexion.z2.auth.impl.SessionImpl.Companion.sessionImpl
import hu.simplexion.z2.auth.model.CredentialType.ACTIVATION_KEY
import hu.simplexion.z2.auth.model.CredentialType.PASSWORD
import hu.simplexion.z2.auth.model.CredentialType.PASSWORD_RESET_KEY
import hu.simplexion.z2.auth.model.Credentials
import hu.simplexion.z2.auth.model.Principal
import hu.simplexion.z2.auth.model.Role
import hu.simplexion.z2.auth.table.CredentialsTable.Companion.credentialsTable
import hu.simplexion.z2.auth.table.PrincipalTable.Companion.principalTable
import hu.simplexion.z2.auth.table.RoleGrantTable.Companion.roleGrantTable
import hu.simplexion.z2.auth.util.BCrypt
import hu.simplexion.z2.baseStrings
import hu.simplexion.z2.commons.util.UUID
import hu.simplexion.z2.history.util.securityHistory
import hu.simplexion.z2.schematic.ensureValid
import hu.simplexion.z2.service.ServiceImpl
import kotlinx.datetime.Clock.System.now

class PrincipalImpl : PrincipalApi, ServiceImpl<PrincipalImpl> {

    companion object {
        val principalImpl = PrincipalImpl().internal

        var addRoles = emptyArray<Role>()
        var getRoles = emptyArray<Role>()
        var updateRoles = emptyArray<Role>()
    }

    override suspend fun list(): List<Principal> {
        ensureAny(*getRoles)
        return principalTable.list()
    }

    override suspend fun add(
        principal: Principal,
        activated: Boolean,
        activationKey: String?,
        roles: List<UUID<Role>>
    ): UUID<Principal> {

        ensureAny(*addRoles)
        ensureValid(principal, true)

        val uuid = principalTable.insert(principal)

        securityHistory(baseStrings.account, baseStrings.add, uuid, principal, roles.joinToString("\n"))

        if (activationKey != null) {
            val credentials = Credentials()
            credentials.type = ACTIVATION_KEY
            credentials.principal = uuid
            credentials.value = BCrypt.hashpw(activationKey, BCrypt.gensalt())
            credentialsTable.insert(credentials)
        }

        for (role in roles) {
            roleGrantTable.insert(role, uuid, null)
        }

        return uuid
    }

    override suspend fun add(credentials: Credentials, currentCredentials: Credentials?) {
        ensureSelfOrSecurityOfficer(credentials.principal)
        securityHistory(baseStrings.account, baseStrings.changeCredentials, credentials.principal, credentials.type)

        if (! isSecurityOfficer || credentials.principal == serviceContext.principal) {
            requireNotNull(currentCredentials)
            sessionImpl(serviceContext).authenticate(credentials.principal, currentCredentials.value, true, currentCredentials.type, authAdminImpl.getPolicy())
        }

        credentials.createdAt = now()
        credentials.value = BCrypt.hashpw(credentials.value, BCrypt.gensalt())

        credentialsTable.insert(credentials)
    }

    override suspend fun get(uuid: UUID<Principal>): Principal {
        ensure(serviceContext.hasAny(*getRoles) or serviceContext.isPrincipal(uuid))
        return principalTable.get(uuid)
    }

    override suspend fun activate(credentials: Credentials, key: Credentials) : String {
        publicAccess()
        sessionImpl(serviceContext).authenticate(key.principal, key.value, true, ACTIVATION_KEY, authAdminImpl.getPolicy())

        securityHistory(baseStrings.account, baseStrings.setActivated, key.principal, true)

        credentials.type = PASSWORD
        credentials.createdAt = now()
        credentials.principal = key.principal
        credentials.value = BCrypt.hashpw(credentials.value, BCrypt.gensalt())

        credentialsTable.removeActivationKeys(key.principal)
        credentialsTable.insert(credentials)
        principalTable.setActivated(key.principal, true)

        return principalTable[key.principal].name
    }

    override suspend fun resetPassword(credentials: Credentials, key: Credentials) : String {
        publicAccess()
        sessionImpl(serviceContext).authenticate(key.principal, key.value, true, PASSWORD_RESET_KEY, authAdminImpl.getPolicy())

        securityHistory(baseStrings.account, baseStrings.passwordReset, key.principal, true)

        credentials.type = PASSWORD
        credentials.createdAt = now()
        credentials.principal = key.principal
        credentials.value = BCrypt.hashpw(credentials.value, BCrypt.gensalt())

        credentialsTable.removePasswordResetKeys(key.principal)
        credentialsTable.insert(credentials)

        return principalTable[key.principal].name
    }

    override suspend fun setActivated(uuid: UUID<Principal>, activated: Boolean) {
        ensureAny(*updateRoles)
        securityHistory(baseStrings.account, baseStrings.setActivated, uuid, activated)

        principalTable.setActivated(uuid, activated)
    }

    override suspend fun setLocked(uuid: UUID<Principal>, locked: Boolean) {
        ensureAny(*updateRoles)
        securityHistory(baseStrings.account, baseStrings.setLocked, uuid, locked)

        principalTable.setLocked(uuid, locked)
    }

}