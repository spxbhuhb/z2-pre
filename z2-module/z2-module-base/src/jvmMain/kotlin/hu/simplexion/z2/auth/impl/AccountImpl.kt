package hu.simplexion.z2.auth.impl

import hu.simplexion.z2.auth.*
import hu.simplexion.z2.auth.api.AccountApi
import hu.simplexion.z2.auth.context.ensure
import hu.simplexion.z2.auth.context.has
import hu.simplexion.z2.auth.context.isAccount
import hu.simplexion.z2.auth.model.*
import hu.simplexion.z2.auth.table.AccountCredentialsTable.Companion.accountCredentialsTable
import hu.simplexion.z2.auth.table.AccountPrivateTable.Companion.accountPrivateTable
import hu.simplexion.z2.auth.table.AccountStatusTable.Companion.accountStatusTable
import hu.simplexion.z2.auth.table.RoleGrantTable.Companion.roleGrantTable
import hu.simplexion.z2.auth.ui.authStrings
import hu.simplexion.z2.auth.util.BCrypt
import hu.simplexion.z2.commons.i18n.commonStrings
import hu.simplexion.z2.commons.util.UUID
import hu.simplexion.z2.history.util.securityHistory
import hu.simplexion.z2.schematic.runtime.dump
import hu.simplexion.z2.service.runtime.ServiceImpl

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
        credentials: AccountCredentials,
        roles : List<UUID<Role>>
    ) : UUID<AccountPrivate> {

        ensure(securityOfficerRole)

        val accountUuid = accountPrivateTable.insert(account)

        securityHistory(authStrings.account, commonStrings.add, accountUuid, account, roles.joinToString("\n"))

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
        securityHistory(authStrings.account, authStrings.changeCredentials, credentials.account, credentials.type, credentials.account)

        // TODO enforce security policy
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

    override suspend fun lock(uuid: UUID<AccountPrivate>) {
        ensure(securityOfficerRole)
        securityHistory(authStrings.account, authStrings.setLocked, uuid, true)

        accountStatusTable.setLocked(uuid, true)
    }

    override suspend fun unlock(uuid: UUID<AccountPrivate>) {
        ensure(securityOfficerRole)
        securityHistory(authStrings.account, authStrings.setLocked, uuid, false)

        accountStatusTable.setLocked(uuid, true)
    }

    override suspend fun getPolicy(): SecurityPolicy {
        TODO("Not yet implemented")
    }

    override suspend fun changePolicy(policy: SecurityPolicy) {
        TODO("Not yet implemented")
    }

}