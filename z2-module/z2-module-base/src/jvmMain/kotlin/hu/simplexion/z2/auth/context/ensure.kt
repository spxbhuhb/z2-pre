package hu.simplexion.z2.auth.context

import hu.simplexion.z2.auth.model.Role
import hu.simplexion.z2.auth.securityOfficerRole
import hu.simplexion.z2.service.runtime.ServiceImpl

/**
 * Ensures that the block runs only when there is no service context for the call.
 *
 * This happens only when the context-less version of the function is called internally
 * on the server side. The context-less version cannot be called through dispatch.
 *
 * @throws   AccessDenied  There is a service context.
 */
fun <T> ServiceImpl.ensureInternal(block : () -> T) : T {
    if (serviceContext.isInternal() != ContextCheckResult.Allow) throw AccessDenied()
    return block()
}

/**
 * Ensures there is no service context for the call.
 *
 * This happens only when the context-less version of the function is called internally
 * on the server side. The context-less version cannot be called through dispatch.
 *
 * @throws   AccessDenied  There is a service context.
 */
fun ServiceImpl.ensureInternal() {
    if (serviceContext.isInternal() != ContextCheckResult.Allow) throw AccessDenied()
}

/**
 * Ensures that the block runs only when there is an account in the context
 * (Session.account != null).
 *
 * @throws   AccessDenied  There is no account in the context.
 */
fun <T> ServiceImpl.ensureLoggedIn(block : () -> T) : T {
    if (serviceContext.isLoggedIn() != ContextCheckResult.Allow) throw AccessDenied()
    return block()
}

/**
 * Ensures that the block runs only when the account has the security officer role.
 *
 * @throws   AccessDenied  The account does not have the security officer role.
 */
fun <T> ServiceImpl.ensureSecurityOfficer(block : () -> T) : T {
    ensure(securityOfficerRole)
    return block()
}

/**
 * Ensures that the account has the security officer role.
 *
 * @throws   AccessDenied  The account does not have the security officer role.
 */
fun ServiceImpl.ensureSecurityOfficer() {
    ensure(securityOfficerRole)
}

/**
 * Ensures that there is an account in the context (Session.account != null).
 *
 * @throws   AccessDenied  There is no account in the context.
 */
fun ServiceImpl.ensureLoggedIn()  {
    if (serviceContext.isLoggedIn() != ContextCheckResult.Allow) throw AccessDenied()
}

/**
 * Ensures that the block runs only when there context contains **ALL**
 * of the specified roles.
 *
 * @throws   AccessDenied  At least one of the roles is not in the context.
 */
fun ServiceImpl.ensure(vararg roles: Role) {
    if (serviceContext.hasAll(*roles) != ContextCheckResult.Allow) throw AccessDenied()
}

/**
 * Ensures that the block runs only when there context contains **ALL**
 * of the specified roles.
 *
 * @throws   AccessDenied  At least one of the roles is not in the context.
 */
fun <T> ServiceImpl.ensure(vararg roles: Role, block : () -> T) : T {
    if (serviceContext.hasAll(*roles) != ContextCheckResult.Allow) throw AccessDenied()
    return block()
}

/**
 * Ensures that the block runs only when there context contains **ANY**
 * of the specified roles.
 *
 * @throws   AccessDenied  None of the roles are in the context.
 */
fun <T> ServiceImpl.ensureAny(vararg roles: Role, block : () -> T) : T {
    if (serviceContext.hasAny(*roles) != ContextCheckResult.Allow) throw AccessDenied()
    return block()
}

/**
 * Ensures that the [result] is [ContextCheckResult.Allow].
 *
 * @throws   AccessDenied  [result] is [ContextCheckResult.Deny]
 */
fun ensure(result : ContextCheckResult) {
    if (result != ContextCheckResult.Allow) throw AccessDenied()
}

/**
 * Ensures that the block runs only when [result] is [ContextCheckResult.Allow].
 *
 * @throws   AccessDenied  [result] is [ContextCheckResult.Deny]
 */
fun <T> ensure(result : ContextCheckResult, block : () -> T) : T {
    if (result != ContextCheckResult.Allow) throw AccessDenied()
    return block()
}

/**
 * Allows the call without any security checks.
 */
inline fun <T> ensuredByLogic(@Suppress("UNUSED_PARAMETER") explanation: String, block : () -> T) : T {
    return block()
}

/**
 * Marks the following code as secure by some logic.
 */
fun ensuredByLogic(@Suppress("UNUSED_PARAMETER") explanation: String) {
    // nothing to do here, this is just a marker
}