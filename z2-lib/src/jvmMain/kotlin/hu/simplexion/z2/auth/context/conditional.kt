package hu.simplexion.z2.auth.context

import hu.simplexion.z2.auth.model.Principal
import hu.simplexion.z2.auth.model.Role
import hu.simplexion.z2.auth.model.Session
import hu.simplexion.z2.auth.securityOfficerRole
import hu.simplexion.z2.services.ServiceContext
import hu.simplexion.z2.services.ServiceImpl
import hu.simplexion.z2.util.UUID

enum class ContextCheckResult {
    Allow,
    Deny;

    val isAllowed
        get() = this == Allow

    infix fun and(other: ContextCheckResult): ContextCheckResult {
        if (this == Allow && other == Allow) return Allow
        return Deny
    }

    infix fun or(other: ContextCheckResult): ContextCheckResult {
        if (this == Allow || other == Allow) return Allow
        return Deny
    }

    operator fun <T> invoke(block: () -> T) {
        if (this == Allow) {
            block()
        } else {
            throw AccessDenied()
        }
    }
}

/**
 * Allow when there is no service context for the call.
 * This happens only when the context-less version of the function is called internally
 * on the server side. The context-less version cannot be called through dispatch.
 *
 * Deny otherwise.
 */
fun ServiceContext.isInternal(): ContextCheckResult {
    if (! this.uuid.isNil) return ContextCheckResult.Deny
    // FIXME principal check for isInternal
    // if (this.principal != null) return ContextCheckResult.Deny
    return ContextCheckResult.Allow
}

/**
 * Allow when [SessionServiceContext.principal != null].
 *
 * Deny otherwise.
 */
fun ServiceContext.isLoggedIn(): ContextCheckResult {
    val session = getSessionOrNull() ?: return ContextCheckResult.Deny
    if (session.principal == null) return ContextCheckResult.Deny
    return ContextCheckResult.Allow
}

/**
 * Allow when the role is in [Session.roles].
 *
 * Deny otherwise.
 */
fun ServiceContext.has(role: Role): ContextCheckResult {
    val session = getSessionOrNull() ?: return ContextCheckResult.Deny
    if (! session.roles.any { it.uuid == role.uuid }) return ContextCheckResult.Deny
    return ContextCheckResult.Allow
}

/**
 * Allow when **ALL** the roles are in [Session.roles].
 *
 * Deny otherwise.
 */
fun ServiceContext.hasAll(vararg roles: Role): ContextCheckResult {
    val session = getSessionOrNull() ?: return ContextCheckResult.Deny
    for (role in roles) {
        if (! session.roles.any { it.uuid == role.uuid }) return ContextCheckResult.Deny
    }
    return ContextCheckResult.Allow
}

/**
 * Allow when **ANY** the roles are in [Session.roles].
 *
 * Deny otherwise.
 */
fun ServiceContext.hasAny(vararg roles: Role): ContextCheckResult {
    val session = getSessionOrNull() ?: return ContextCheckResult.Deny
    for (role in roles) {
        if (session.roles.any { it.uuid == role.uuid }) return ContextCheckResult.Allow
    }
    return ContextCheckResult.Deny
}

/**
 * Allow when **ANY** the roles are in [Session.roles].
 *
 * Deny otherwise.
 */
fun ServiceContext.hasAny(vararg roles: UUID<Role>): ContextCheckResult {
    val session = getSessionOrNull() ?: return ContextCheckResult.Deny
    for (role in roles) {
        if (session.roles.any { it.uuid == role }) return ContextCheckResult.Allow
    }
    return ContextCheckResult.Deny
}

/**
 * Allow when [Session.principal] is [principal].
 *
 * Deny otherwise.
 */
fun ServiceContext.isPrincipal(principal: UUID<Principal>): ContextCheckResult {
    val session = getSessionOrNull() ?: return ContextCheckResult.Deny
    if (principal != session.principal) return ContextCheckResult.Deny
    return ContextCheckResult.Allow
}

/**
 * Get [Session.principal].
 *
 * @throws  IllegalStateException  when there is no session or there is no principal
 */
val ServiceContext.principal
    get() = checkNotNull(getSessionOrNull()?.principal) { "there is no session in the service context" }

/**
 * Get [Session.principal] of present, null if it doesn't.
 */
val ServiceContext.principalOrNull
    get() = getSessionOrNull()?.principal

/**
 * True when [Session.principal] is [principal], false otherwise.
 */
fun ServiceImpl<*>.isSelf(principal : UUID<*>) : Boolean {
    return serviceContext.principalOrNull == principal
}

/**
 * True when the session has [securityOfficerRole].
 */
val ServiceImpl<*>.isSecurityOfficer
    get() = serviceContext.has(securityOfficerRole).isAllowed

