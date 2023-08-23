package hu.simplexion.z2.auth.impl

import hu.simplexion.z2.auth.*
import hu.simplexion.z2.auth.api.AccountApi
import hu.simplexion.z2.auth.context.ensure
import hu.simplexion.z2.auth.context.has
import hu.simplexion.z2.auth.context.isAccount
import hu.simplexion.z2.auth.model.*
import hu.simplexion.z2.auth.ui.authStrings
import hu.simplexion.z2.auth.util.BCrypt
import hu.simplexion.z2.commons.util.UUID
import hu.simplexion.z2.history.securityHistory
import hu.simplexion.z2.schematic.runtime.dump
import hu.simplexion.z2.service.runtime.ServiceImpl

class AccountImpl: AccountApi, ServiceImpl {

    override suspend fun list(): List<AccountPrivate> {
        ensure(securityOfficerRole)
        return accountPrivateTable.list()
    }

    override suspend fun add(
        account: AccountPrivate,
        credentials: AccountCredentials,
        roles : List<UUID<Role>>
    ) : UUID<AccountPrivate> {

        ensure(securityOfficerRole)
        securityHistory(authStrings.addAccount, account.dump(), roles.joinToString("\n"))

        val accountUuid = accountPrivateTable.insert(account)

        credentials.account = accountUuid
        credentials.value = BCrypt.hashpw(credentials.value, BCrypt.gensalt())

        accountCredentialsTable.insert(credentials)

        for(role in roles) {
            roleGrantTable.insert(role, accountUuid, null)
        }
        
        return accountUuid
    }

    override suspend fun add(credentials: AccountCredentials) {
        ensure(securityOfficerRole)
        securityHistory(authStrings.changeCredentials, credentials.type, credentials.account)

        // TODO enforce security policy
        accountCredentialsTable.insert(credentials)
    }

    override suspend fun get(uuid: UUID<AccountPrivate>): AccountPrivate {
        ensure(serviceContext.has(securityOfficerRole) or serviceContext.isAccount(uuid))
        return accountPrivateTable.get(uuid)
    }
    
    override suspend fun status(uuid: UUID<AccountStatus>): AccountStatus {
        ensure(securityOfficerRole)
        return accountStatusTable.get(uuid)
    }

    override suspend fun lock(uuid: UUID<AccountPrivate>) {
        ensure(securityOfficerRole)
        securityHistory(authStrings.setLocked, uuid, true)

        accountStatusTable.setLocked(uuid, true)
    }

    override suspend fun unlock(uuid: UUID<AccountPrivate>) {
        ensure(securityOfficerRole)
        securityHistory(authStrings.setLocked, uuid, false)

        accountStatusTable.setLocked(uuid, true)
    }

    override suspend fun getPolicy(): SecurityPolicy {
        TODO("Not yet implemented")
    }

    override suspend fun changePolicy(policy: SecurityPolicy) {
        TODO("Not yet implemented")
    }

}