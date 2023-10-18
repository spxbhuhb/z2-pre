package hu.simplexion.z2.auth.impl

import hu.simplexion.z2.auth.anonymousUuid
import hu.simplexion.z2.auth.api.SessionApi
import hu.simplexion.z2.auth.context.*
import hu.simplexion.z2.auth.model.CredentialType
import hu.simplexion.z2.auth.model.Principal
import hu.simplexion.z2.auth.model.Role
import hu.simplexion.z2.auth.model.Session
import hu.simplexion.z2.auth.model.Session.Companion.SESSION_TOKEN_UUID
import hu.simplexion.z2.auth.securityOfficerRole
import hu.simplexion.z2.auth.securityPolicy
import hu.simplexion.z2.auth.table.CredentialsTable.Companion.credentialsTable
import hu.simplexion.z2.auth.table.PrincipalTable.Companion.principalTable
import hu.simplexion.z2.auth.table.RoleGrantTable.Companion.roleGrantTable
import hu.simplexion.z2.auth.table.SessionTable.Companion.sessionTable
import hu.simplexion.z2.auth.util.BCrypt
import hu.simplexion.z2.auth.util.Unauthorized
import hu.simplexion.z2.baseStrings
import hu.simplexion.z2.commons.util.UUID
import hu.simplexion.z2.history.util.securityHistory
import hu.simplexion.z2.service.ServiceContext
import hu.simplexion.z2.service.ServiceImpl
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

    override suspend fun owner(): UUID<Principal>? {
        ensuredByLogic("Session owner gets its own principal.")
        return serviceContext.getSessionOrNull()?.principal
    }

    override suspend fun roles(): List<Role> {
        ensuredByLogic("Session owner gets its own roles.")
        return serviceContext.getSessionOrNull()?.roles ?: emptyList()
    }

    override suspend fun login(name: String, password: String): Session {
        val principal = principalTable.getByNameOrNull(name)

        if (principal == null) {
            securityHistory(anonymousUuid, baseStrings.account, baseStrings.authenticateFail, baseStrings.accountNotFound)
            throw AccessDenied()
        }

        try {
            authenticate(principal.uuid, password, true, CredentialType.PASSWORD)
        } catch (ex: Unauthorized) {
            // FIXME, is locked meaningful? if we send it to the user it is possible to find principal names by N failed auth
            securityHistory(anonymousUuid, baseStrings.account, baseStrings.authenticateFail, ex.reason, ex.locked)
            throw AccessDenied()
        }

        val session = Session().also {
            it.uuid = serviceContext!!.uuid
            it.principal = principal.uuid
            it.roles = roleGrantTable.rolesOf(principal.uuid, null)
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
        securityHistory(baseStrings.account, baseStrings.logout, serviceContext.principal)
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
    private val authenticateInProgress = mutableSetOf<UUID<Principal>>()

    fun authenticate(
        principalId: UUID<Principal>,
        password: String,
        checkCredentials: Boolean,
        credentialType: String,
    ) {

        // FIXME check credential expiration

        val validCredentials = if (checkCredentials) {
            val credential = credentialsTable.readValue(principalId, credentialType) ?: throw Unauthorized("NoCredential")
            BCrypt.checkpw(password, credential)
        } else true

        // this is here to prevent SQL deadlocks
        lockState(principalId)

        try {
            val principal = principalTable.get(principalId)

            val result = when {
                ! principal.activated -> Unauthorized("NotValidated")
                principal.locked -> Unauthorized("Locked", true)
                principal.expired -> Unauthorized("Expired")
                principal.anonymized -> Unauthorized("Anonymized")
                ! validCredentials -> Unauthorized("InvalidCredentials")
                else -> null
            }

            if (result != null) {
                principal.authFailCount ++
                principal.lastAuthFail = Clock.System.now()
                principal.locked = principal.locked || (principal.authFailCount > securityPolicy.maxFailedAuths)

                principalTable.update(principal.uuid, principal)
                securityHistory(principalId, baseStrings.account, baseStrings.authenticateFail, principalId, result.reason, result.locked)

                TransactionManager.current().commit()

                throw result
            }

            principal.lastAuthSuccess = Clock.System.now()
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
}