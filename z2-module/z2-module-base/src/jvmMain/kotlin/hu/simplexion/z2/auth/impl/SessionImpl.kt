package hu.simplexion.z2.auth.impl

import hu.simplexion.z2.auth.*
import hu.simplexion.z2.auth.api.SessionApi
import hu.simplexion.z2.auth.context.ensure
import hu.simplexion.z2.auth.context.ensureLoggedIn
import hu.simplexion.z2.auth.context.ensuredByLogic
import hu.simplexion.z2.auth.context.getSessionOrNull
import hu.simplexion.z2.auth.model.AccountPrivate
import hu.simplexion.z2.auth.model.CredentialType
import hu.simplexion.z2.auth.model.Session
import hu.simplexion.z2.auth.model.Session.Companion.SESSION_TOKEN_UUID
import hu.simplexion.z2.auth.ui.authStrings
import hu.simplexion.z2.auth.util.BCrypt
import hu.simplexion.z2.auth.util.Unauthorized
import hu.simplexion.z2.commons.util.UUID
import hu.simplexion.z2.history.securityHistory
import hu.simplexion.z2.service.runtime.ServiceImpl
import kotlinx.datetime.Clock
import org.jetbrains.exposed.sql.transactions.TransactionManager
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock

class SessionImpl: SessionApi, ServiceImpl {

    // ----------------------------------------------------------------------------------
    // API functions
    // ----------------------------------------------------------------------------------

    override suspend fun owner(): UUID<AccountPrivate>? {
        ensuredByLogic("Session owner gets its own account.")
        return serviceContext.getSessionOrNull()?.account
    }

    override suspend fun roles(): List<String> {
        ensuredByLogic("Session owner gets its own roles.")
        return serviceContext.getSessionOrNull()?.roles?.split(";") ?: emptyList()
    }

    override suspend fun login(name: String, password: String): Int {
        val account = accountPrivateTable.getByAccountNameOrNull(name)

        if (account == null) {
            securityHistory(anonymousUuid, authStrings.loginFail, authStrings.accountNotFound)
            return -1
        }

        try {
            authenticate(account.uuid, password)
        } catch (ex : Unauthorized) {
            // FIXME, is locked meaningful? if we send it to the user it is possible to find account names by N failed auth
            securityHistory(anonymousUuid, authStrings.loginFail, ex.reason, ex.locked)
            return -1
        }

        requireNotNull(serviceContext).data[SESSION_TOKEN_UUID] = Session().also {
            it.account = account.uuid
            it.fullName = account.fullName
            it.roles = roleGrantTable.rolesOf(account.uuid, null).joinToString { r -> r.programmaticName }
        }

        return 0
    }

    override suspend fun logout() {
        ensureLoggedIn()
        securityHistory(authStrings.logout, serviceContext.getSessionOrNull()?.uuid)

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

    private fun authenticate(
        accountId: UUID<AccountPrivate>,
        password: String,
        checkCredentials: Boolean = true
    ) {

        val validCredentials = if (checkCredentials) {
            val credential = accountCredentialsTable.readValue(accountId, CredentialType.PASSWORD) ?: throw Unauthorized("NoCredential")
            BCrypt.checkpw(password, credential)
        } else true

        lockState(accountId)

        try {
            val state = accountStatusTable.readOrNull(accountId)

            val result = when {
                state == null -> throw Unauthorized("NoState")
                ! state.validated -> Unauthorized("NotValidated")
                state.locked -> Unauthorized("Locked", true)
                state.expired -> Unauthorized("Expired")
                state.anonymized -> Unauthorized("Anonymized")
                ! validCredentials -> Unauthorized("InvalidCredentials")
                else -> null
            }

            if (result != null) {
                state.loginFailCount ++
                state.lastLoginFail = Clock.System.now()
                state.locked = state.locked || (state.loginFailCount > securityPolicy.maxFailedLogins)

                accountStatusTable.update(state.uuid, state)
                securityHistory(accountId, authStrings.loginFail, result.reason, result.locked)

                TransactionManager.current().commit()

                throw result
            }

            state.lastLoginSuccess = Clock.System.now()
            state.loginSuccessCount ++

            state.loginFailCount = 0

            securityHistory(accountId, authStrings.loginSuccess)

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