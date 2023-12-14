package hu.simplexion.z2.auth.impl

import hu.simplexion.z2.auth.anonymousUuid
import hu.simplexion.z2.auth.api.SessionApi
import hu.simplexion.z2.auth.context.*
import hu.simplexion.z2.auth.impl.AuthAdminImpl.Companion.authAdminImpl
import hu.simplexion.z2.auth.model.*
import hu.simplexion.z2.auth.model.CredentialType.ACTIVATION_KEY
import hu.simplexion.z2.auth.model.Session.Companion.LOGOUT_TOKEN_UUID
import hu.simplexion.z2.auth.model.Session.Companion.SESSION_TOKEN_UUID
import hu.simplexion.z2.auth.securityOfficerRole
import hu.simplexion.z2.auth.table.CredentialsTable.Companion.credentialsTable
import hu.simplexion.z2.auth.table.PrincipalTable.Companion.principalTable
import hu.simplexion.z2.auth.table.RoleGrantTable.Companion.roleGrantTable
import hu.simplexion.z2.auth.table.SessionTable.Companion.sessionTable
import hu.simplexion.z2.auth.util.AuthenticationFail
import hu.simplexion.z2.auth.util.BCrypt
import hu.simplexion.z2.baseStrings
import hu.simplexion.z2.commons.util.UUID
import hu.simplexion.z2.commons.util.fourRandomInt
import hu.simplexion.z2.commons.util.vmNowSecond
import hu.simplexion.z2.history.util.securityHistory
import hu.simplexion.z2.localization.text.LocalizedText
import hu.simplexion.z2.service.ServiceContext
import hu.simplexion.z2.service.ServiceImpl
import hu.simplexion.z2.site.boot.housekeepingScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.datetime.Clock.System.now
import org.jetbrains.exposed.sql.transactions.TransactionManager
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock
import kotlin.math.abs

class SessionImpl : SessionApi, ServiceImpl<SessionImpl> {

    companion object {
        val sessionImpl = SessionImpl().internal

        /**
         * Sessions waiting for the second step of 2FA.
         */
        private val preparedSessions = ConcurrentHashMap<UUID<ServiceContext>, Session>()

        /**
         * Active sessions used for authorization. Use the [getSessionForContext] method
         * to get the session.
         */
        private val activeSessions = ConcurrentHashMap<UUID<ServiceContext>, Session>()

        /**
         * Function to send the security code.
         */
        var sendSecurityCode: (session: Session) -> Unit = { }

        suspend fun sessionExpiration() {
            while (housekeepingScope.isActive) {
                val policy = transaction { runBlocking { authAdminImpl.getPolicy() } } // TODO cache policy

                val now = vmNowSecond()
                val next = now + 60

                val cutoff = now - policy.sessionExpirationInterval * 60 // any last activity before the cutoff is too old

                activeSessions.values
                    .filter {
                        println("$now $next $cutoff ${it.lastActivity} ${it.lastActivity < cutoff}")
                        it.lastActivity < cutoff
                    }
                    .forEach { session ->
                        activeSessions.remove(session.uuid)
                        transaction {
                            session.history(baseStrings.expired)
                        }
                    }

                while (housekeepingScope.isActive) {
                    val preparedNow = vmNowSecond()
                    val preparedCutoff = now - policy.sessionActivationInterval * 60 // any last activity before the cutoff is too old
                    if (preparedNow > next) break

                    preparedSessions.values
                        .filter { it.vmCreatedAt < preparedCutoff }
                        .forEach { session ->
                            preparedSessions.remove(session.uuid)
                            transaction {
                                session.history(baseStrings.expiredSecurityCode)
                            }
                        }

                    delay(5000)
                }
            }
        }

        private fun Session.history(event: LocalizedText) {
            securityHistory(principal, baseStrings.session, event, baseStrings.session, uuid, baseStrings.account, principal)
        }

        init {
            housekeepingScope.launch { sessionExpiration() }
        }
    }

    // ----------------------------------------------------------------------------------
    // API functions
    // ----------------------------------------------------------------------------------

    override suspend fun owner(): UUID<Principal>? {
        ensuredByLogic("Session owner gets own principal.")
        return serviceContext.getSessionOrNull()?.principal
    }

    override suspend fun roles(): List<Role> {
        ensuredByLogic("Session owner gets own roles.")
        return serviceContext.getSessionOrNull()?.roles ?: emptyList()
    }

    override suspend fun login(name: String, password: String): Session {
        val principal = principalTable.getByNameOrNull(name)

        if (principal == null) {
            securityHistory(anonymousUuid, baseStrings.account, baseStrings.authenticateFail, baseStrings.accountNotFound)
            throw AuthenticationFail("Unknown", false)
        }

        try {
            authenticate(principal.uuid, password, true, CredentialType.PASSWORD, authAdminImpl.getPolicy())
        } catch (ex: AuthenticationFail) {
            securityHistory(anonymousUuid, baseStrings.account, baseStrings.authenticateFail, ex.reason, ex.locked)
            throw ex
        }

        val session = Session().also {
            it.uuid = serviceContext.uuid
            it.principal = principal.uuid
            it.vmCreatedAt = vmNowSecond()
            it.lastActivity = it.vmCreatedAt
            it.roles = roleGrantTable.rolesOf(principal.uuid, null)
        }

        session.history(baseStrings.created)

        if (authAdminImpl.getPolicy().twoFactorAuthentication) {
            preparedSessions[serviceContext.uuid] = session
            session.securityCode = abs(fourRandomInt()[0]).toString().padStart(6, '0').substring(0, 6)
            sendSecurityCode(session)
        } else {
            activeSessions[serviceContext.uuid] = session
            serviceContext.data[SESSION_TOKEN_UUID] = session
        }

        return session
    }

    override suspend fun activateSession(securityCode: String): Session {
        val session = preparedSessions[serviceContext.uuid] ?: throw AccessDenied()
        if (session.securityCode != securityCode) throw AuthenticationFail("Code")

        session.history(baseStrings.securityCodeOk)

        preparedSessions.remove(serviceContext.uuid)
        activeSessions[serviceContext.uuid] = session
        serviceContext.data[SESSION_TOKEN_UUID] = session

        return session
    }

    override suspend fun getSession(): Session? {
        return serviceContext.getSessionOrNull()
    }

    override suspend fun logout() {
        publicAccess()

        serviceContext.data[LOGOUT_TOKEN_UUID] = true

        if (serviceContext.getSessionOrNull() == null) return

        securityHistory(baseStrings.account, baseStrings.logout, serviceContext.principal)
        serviceContext.getSession().history(baseStrings.removed)

        activeSessions.remove(serviceContext.uuid)
        serviceContext.data.remove(SESSION_TOKEN_UUID)
    }

    override suspend fun logout(session: UUID<Session>) {
        ensureAll(securityOfficerRole)
        // TODO forced logout
    }

    override suspend fun list(): List<Session> {
        ensureAll(securityOfficerRole)
        return sessionTable.query()
    }

    // ----------------------------------------------------------------------------------
    // Non-API functions
    // ----------------------------------------------------------------------------------

    private val authenticateLock = ReentrantLock()
    private val authenticateInProgress = mutableSetOf<UUID<Principal>>()

    fun authenticate(
        principalId: UUID<Principal>,
        password: String,
        checkCredentials: Boolean,
        credentialType: String,
        policy : SecurityPolicy
    ) {

        // FIXME check credential expiration

        val validCredentials = if (checkCredentials) {
            val credential = credentialsTable.readValue(principalId, credentialType) ?: throw AuthenticationFail("NoCredential")
            BCrypt.checkpw(password, credential)
        } else true

        // this is here to prevent SQL deadlocks
        lockState(principalId)

        try {
            val principal = principalTable[principalId]

            val result = when {
                ! principal.activated && credentialType != ACTIVATION_KEY -> AuthenticationFail("NotActivated")
                principal.locked -> AuthenticationFail("Locked", true)
                principal.expired -> AuthenticationFail("Expired")
                principal.anonymized -> AuthenticationFail("Anonymized")
                ! validCredentials -> AuthenticationFail("InvalidCredentials")
                else -> null
            }

            if (result != null) {
                principal.authFailCount ++
                principal.lastAuthFail = now()
                principal.locked = principal.locked || (principal.authFailCount > policy.maxFailedAuths)

                principalTable.update(principal.uuid, principal)
                securityHistory(principalId, baseStrings.account, baseStrings.authenticateFail, principalId, result.reason, result.locked)

                TransactionManager.current().commit()

                throw result
            }

            principal.lastAuthSuccess = now()
            principal.authSuccessCount ++
            principal.authFailCount = 0

            principalTable.update(principal.uuid, principal)
            securityHistory(principalId, baseStrings.account, baseStrings.authenticateSuccess, principalId)

            TransactionManager.current().commit()

        } finally {
            releaseState(principalId)
        }
    }

    private fun lockState(principalId: UUID<Principal>) {
        var success = false
        for (tryNumber in 1..5) {
            success = authenticateLock.withLock {
                if (principalId in authenticateInProgress) {
                    Thread.sleep(100)
                    false
                } else {
                    authenticateInProgress += principalId
                    true
                }
            }
            if (success) break
        }
        if (! success) throw RuntimeException("couldn't lock principal state in 5 tries")
    }

    private fun releaseState(principalId: UUID<Principal>) {
        authenticateLock.withLock {
            authenticateInProgress -= principalId
        }
    }

    fun getSessionForContext(sessionUuid: UUID<ServiceContext>): Session? =
        activeSessions.computeIfPresent(sessionUuid) { _, session ->
            session.lastActivity = vmNowSecond()
            session
        }

}