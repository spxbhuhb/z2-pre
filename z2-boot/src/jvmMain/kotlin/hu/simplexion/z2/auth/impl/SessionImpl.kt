package hu.simplexion.z2.auth.impl

import hu.simplexion.z2.auth.anonymousUuid
import hu.simplexion.z2.auth.api.SessionApi
import hu.simplexion.z2.auth.context.*
import hu.simplexion.z2.auth.model.AccountPrivate
import hu.simplexion.z2.auth.model.CredentialType
import hu.simplexion.z2.auth.model.Role
import hu.simplexion.z2.auth.model.Session
import hu.simplexion.z2.auth.model.Session.Companion.SESSION_TOKEN_UUID
import hu.simplexion.z2.auth.securityOfficerRole
import hu.simplexion.z2.auth.securityPolicy
import hu.simplexion.z2.auth.table.AccountCredentialsTable.Companion.accountCredentialsTable
import hu.simplexion.z2.auth.table.AccountPrivateTable.Companion.accountPrivateTable
import hu.simplexion.z2.auth.table.AccountStatusTable.Companion.accountStatusTable
import hu.simplexion.z2.auth.table.RoleGrantTable.Companion.roleGrantTable
import hu.simplexion.z2.auth.table.SessionTable.Companion.sessionTable
import hu.simplexion.z2.auth.util.BCrypt
import hu.simplexion.z2.auth.util.Unauthorized
import hu.simplexion.z2.baseStrings
import hu.simplexion.z2.commons.util.UUID
import hu.simplexion.z2.history.util.securityHistory
import hu.simplexion.z2.service.runtime.ServiceContext
import hu.simplexion.z2.service.runtime.ServiceImpl
import kotlinx.datetime.Clock
import org.jetbrains.exposed.sql.transactions.TransactionManager
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock

class SessionImpl : SessionApi, ServiceImpl<SessionImpl> {

    companion object {
        val sessionImpl = SessionImpl()

        val activeSessions = ConcurrentHashMap<UUID<ServiceContext>, Session>()
    }

    // ----------------------------------------------------------------------------------
    // API functions
    // ----------------------------------------------------------------------------------

    override suspend fun owner(): UUID<AccountPrivate>? {
        ensuredByLogic("Session owner gets its own account.")
        return serviceContext.getSessionOrNull()?.account
    }

    override suspend fun roles(): List<Role> {
        ensuredByLogic("Session owner gets its own roles.")
        return serviceContext.getSessionOrNull()?.roles ?: emptyList()
    }

    override suspend fun login(name: String, password: String): Session {
        val account = accountPrivateTable.getByAccountNameOrNull(name)

        if (account == null) {
            securityHistory(anonymousUuid, baseStrings.account, baseStrings.authenticateFail, baseStrings.accountNotFound)
            throw AccessDenied()
        }

        try {
            authenticate(account.uuid, password, true, CredentialType.PASSWORD)
        } catch (ex: Unauthorized) {
            // FIXME, is locked meaningful? if we send it to the user it is possible to find account names by N failed auth
            securityHistory(anonymousUuid, baseStrings.account, baseStrings.authenticateFail, ex.reason, ex.locked)
            throw AccessDenied()
        }

        val session = Session().also {
            it.uuid = serviceContext!!.uuid
            it.account = account.uuid
            it.fullName = account.fullName
            it.roles = roleGrantTable.rolesOf(account.uuid, null)
        }

        requireNotNull(serviceContext).let {
            activeSessions[it.uuid] = session
            it.data[SESSION_TOKEN_UUID] = session
        }

        return session
    }

    override suspend fun getSession(): Session? {
        return serviceContext.getSessionOrNull()
    }

    override suspend fun logout() {
        ensureLoggedIn()
        securityHistory(baseStrings.account, baseStrings.logout, serviceContext.account)
        activeSessions.remove(serviceContext!!.uuid)
        serviceContext?.data?.remove(SESSION_TOKEN_UUID)
    }

    override suspend fun logout(session: UUID<Session>) {
        ensure(securityOfficerRole)
        // TODO forced logout
    }

    override suspend fun list(): List<Session> {
        ensure(securityOfficerRole)
        return sessionTable.list()
    }

    // ----------------------------------------------------------------------------------
    // Non-API functions
    // ----------------------------------------------------------------------------------

    private val authenticateLock = ReentrantLock()
    private val authenticateInProgress = mutableSetOf<UUID<AccountPrivate>>()

    fun authenticate(
        accountId: UUID<AccountPrivate>,
        password: String,
        checkCredentials: Boolean,
        credentialType: String,
    ) {

        // FIXME check credential expiration

        val validCredentials = if (checkCredentials) {
            val credential = accountCredentialsTable.readValue(accountId, credentialType) ?: throw Unauthorized("NoCredential")
            BCrypt.checkpw(password, credential)
        } else true

        // this is here to prevent SQL deadlocks
        lockState(accountId)

        try {
            val state = accountStatusTable.readOrNull(accountId)

            val result = when {
                state == null -> throw Unauthorized("NoState")
                ! state.activated -> Unauthorized("NotValidated")
                state.locked -> Unauthorized("Locked", true)
                state.expired -> Unauthorized("Expired")
                state.anonymized -> Unauthorized("Anonymized")
                ! validCredentials -> Unauthorized("InvalidCredentials")
                else -> null
            }

            if (result != null) {
                state.authFailCount ++
                state.lastAuthFail = Clock.System.now()
                state.locked = state.locked || (state.authFailCount > securityPolicy.maxFailedAuths)

                accountStatusTable.update(state.uuid, state)
                securityHistory(accountId, baseStrings.account, baseStrings.authenticateFail, accountId, result.reason, result.locked)

                TransactionManager.current().commit()

                throw result
            }

            state.lastAuthSuccess = Clock.System.now()
            state.authSuccessCount ++
            state.authFailCount = 0

            accountStatusTable.update(state.uuid, state)
            securityHistory(accountId, baseStrings.account, baseStrings.authenticateSuccess, accountId)

            TransactionManager.current().commit()

        } finally {
            releaseState(accountId)
        }
    }

    private fun lockState(accountId: UUID<AccountPrivate>) {
        var success = false
        for (tryNumber in 1..5) {
            success = authenticateLock.withLock {
                if (accountId in authenticateInProgress) {
                    Thread.sleep(100)
                    false
                } else {
                    authenticateInProgress += accountId
                    true
                }
            }
            if (success) break
        }
        if (! success) throw RuntimeException("couldn't lock account state in 5 tries")
    }

    private fun releaseState(accountId: UUID<AccountPrivate>) {
        authenticateLock.withLock {
            authenticateInProgress -= accountId
        }
    }
}