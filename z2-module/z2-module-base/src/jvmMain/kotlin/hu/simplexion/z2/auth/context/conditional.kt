package hu.simplexion.z2.auth.context

import hu.simplexion.z2.auth.model.AccountPrivate
import hu.simplexion.z2.auth.model.Role
import hu.simplexion.z2.auth.model.Session
import hu.simplexion.z2.commons.util.UUID
import hu.simplexion.z2.service.runtime.ServiceContext

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
fun ServiceContext?.isInternal(): ContextCheckResult {
    if (this != null) return ContextCheckResult.Deny
    return ContextCheckResult.Allow
}

/**
 * Allow when [SessionServiceContext.account != null].
 *
 * Deny otherwise.
 */
fun ServiceContext?.isLoggedIn(): ContextCheckResult {
    val session = getSessionOrNull() ?: return ContextCheckResult.Deny
    if (session.account == null) return ContextCheckResult.Deny
    return ContextCheckResult.Allow
}


/**
 * Allow when **ALL** the roles are in [Session.roles].
 *
 * Deny otherwise.
 */
fun ServiceContext?.has(role: Role): ContextCheckResult {
    val session = getSessionOrNull() ?: return ContextCheckResult.Deny
    if (! session.roles.contains(role.id.toString())) return ContextCheckResult.Deny
    // FIXME if (! session.roles.any { it.id == role.id }) return ContextCheckResult.Deny
    return ContextCheckResult.Allow
}

/**
 * Allow when **ALL** the roles are in [Session.roles].
 *
 * Deny otherwise.
 */
fun ServiceContext?.hasAll(vararg roles: Role): ContextCheckResult {
    val session = getSessionOrNull() ?: return ContextCheckResult.Deny
    for (role in roles) {
        if (! session.roles.contains(role.id.toString())) return ContextCheckResult.Deny
        // FIXME if (! session.roles.any { it.id == role.id }) return ContextCheckResult.Deny
    }
    return ContextCheckResult.Allow
}

/**
 * Allow when **ANY** the roles are in [Session.roles].
 *
 * Deny otherwise.
 */
fun ServiceContext?.hasAny(vararg roles: Role): ContextCheckResult {
    val session = getSessionOrNull() ?: return ContextCheckResult.Deny
    for (role in roles) {
        if (session.roles.contains(role.id.toString())) return ContextCheckResult.Allow
        // FIXME if (session.roles.any { it.id == role.id }) return ContextCheckResult.Allow
    }
    return ContextCheckResult.Deny
}

/**
 * Allow when [Session.account] is [account].
 *
 * Deny otherwise.
 */
fun ServiceContext?.isAccount(account: UUID<AccountPrivate>): ContextCheckResult {
    val session = getSessionOrNull() ?: return ContextCheckResult.Deny
    if (account != session.account) return ContextCheckResult.Deny
    return ContextCheckResult.Allow
}

/**
 * Get [Session.account].
 *
 * @throws  IllegalStateException  when there is no session or there is no account
 */
val ServiceContext?.account
    get() = checkNotNull(getSessionOrNull()?.account) { "there is no session in the service context" }
