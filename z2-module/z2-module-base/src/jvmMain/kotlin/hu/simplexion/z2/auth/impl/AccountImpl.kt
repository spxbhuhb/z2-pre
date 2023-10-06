package hu.simplexion.z2.auth.impl

import hu.simplexion.z2.auth.api.AccountApi
import hu.simplexion.z2.auth.context.*
import hu.simplexion.z2.auth.impl.SessionImpl.Companion.sessionImpl
import hu.simplexion.z2.auth.model.*
import hu.simplexion.z2.auth.model.CredentialType.ACTIVATION_KEY
import hu.simplexion.z2.auth.securityOfficerRole
import hu.simplexion.z2.auth.table.AccountCredentialsTable.Companion.accountCredentialsTable
import hu.simplexion.z2.auth.table.AccountPrivateTable.Companion.accountPrivateTable
import hu.simplexion.z2.auth.table.AccountStatusTable.Companion.accountStatusTable
import hu.simplexion.z2.auth.table.RoleGrantTable.Companion.roleGrantTable
import hu.simplexion.z2.auth.ui.authStrings
import hu.simplexion.z2.auth.util.BCrypt
import hu.simplexion.z2.commons.util.UUID
import hu.simplexion.z2.history.util.securityHistory
import hu.simplexion.z2.service.runtime.ServiceImpl
import kotlinx.datetime.Clock.System.now

class AccountImpl: AccountApi, ServiceImpl<AccountImpl> {

    companion object {
        val accountImpl = AccountImpl()
    }

    override suspend fun list(): List<AccountPrivate> {
        ensure(securityOfficerRole)
        return accountPrivateTable.list()
    }

    override suspend fun add(
        account: AccountPrivate,
        activated : Boolean,
        activationKey : String?,
        roles : List<UUID<Role>>
    ) : UUID<AccountPrivate> {

        ensure(securityOfficerRole)

        val accountUuid = accountPrivateTable.insert(account)

        securityHistory(authStrings.account, authStrings.add, accountUuid, account, roles.joinToString("\n"))

        if (activationKey != null) {
            val credentials = AccountCredentials()
            credentials.type = ACTIVATION_KEY
            credentials.account = accountUuid
            credentials.value = BCrypt.hashpw(activationKey, BCrypt.gensalt())
            accountCredentialsTable.insert(credentials)
        }

        val status = AccountStatus().also {
            it.account = accountUuid
            it.activated = activated
        }

        accountStatusTable.insert(status)

        for(role in roles) {
            roleGrantTable.insert(role, accountUuid, null)
        }
        
        return accountUuid
    }

    override suspend fun add(credentials: AccountCredentials, currentCredentials: AccountCredentials?) {
        ensureSelfOrSecurityOfficer(credentials.account)
        securityHistory(authStrings.account, authStrings.changeCredentials, credentials.account, credentials.type)

        if (!isSecurityOfficer || credentials.account == serviceContext.account) {
            requireNotNull(currentCredentials)
            sessionImpl(serviceContext!!).authenticate(credentials.account, currentCredentials.value, true, currentCredentials.type)
        }

        credentials.createdAt = now()
        credentials.value = BCrypt.hashpw(credentials.value, BCrypt.gensalt())

        accountCredentialsTable.insert(credentials)
    }

    override suspend fun get(uuid: UUID<AccountPrivate>): AccountPrivate {
        ensure(serviceContext.has(securityOfficerRole) or serviceContext.isAccount(uuid))
        return accountPrivateTable.get(uuid)
    }
    
    override suspend fun status(uuid: UUID<AccountPrivate>): AccountStatus {
        ensure(securityOfficerRole)
        return requireNotNull(accountStatusTable.readOrNull(uuid))
    }

    override suspend fun activate(credentials: AccountCredentials, activationKey: AccountCredentials) {
        ensuredByLogic("calls other service function (add) directly")
        add(credentials, activationKey)
        accountStatusTable.setActivated(activationKey.account, true)
    }

    override suspend fun setActivated(uuid: UUID<AccountPrivate>, activated : Boolean) {
        ensure(securityOfficerRole)
        securityHistory(authStrings.account, authStrings.setActivated, uuid, true)

        accountStatusTable.setActivated(uuid, true)
    }

    override suspend fun setLocked(uuid: UUID<AccountPrivate>, locked: Boolean) {
        ensure(securityOfficerRole)
        securityHistory(authStrings.account, authStrings.setLocked, uuid, locked)

        accountStatusTable.setLocked(uuid, locked)
    }

    override suspend fun getPolicy(): SecurityPolicy {
        TODO("Not yet implemented")
    }

    override suspend fun changePolicy(policy: SecurityPolicy) {
        TODO("Not yet implemented")
    }

}