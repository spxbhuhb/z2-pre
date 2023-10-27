package hu.simplexion.z2.auth.context

import hu.simplexion.z2.auth.model.Principal
import hu.simplexion.z2.auth.model.Role
import hu.simplexion.z2.auth.securityOfficerRole
import hu.simplexion.z2.commons.util.UUID
import hu.simplexion.z2.service.ServiceImpl
import hu.simplexion.z2.site.impl.SiteImpl.Companion.siteImpl

/**
 * Ensures that the block runs only when there is no service context for the call.
 *
 * This happens only when the context-less version of the function is called internally
 * on the server side. The context-less version cannot be called through dispatch.
 *
 * @throws   AccessDenied  There is a service context.
 */
fun <T> ServiceImpl<*>.ensureInternal(block: () -> T): T {
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
fun ServiceImpl<*>.ensureInternal() {
    if (serviceContext.isInternal() != ContextCheckResult.Allow) throw AccessDenied()
}

/**
 * Ensures that the block runs only when there is an principal in the context
 * (Session.principal != null).
 *
 * @throws   AccessDenied  There is no principal in the context.
 */
fun <T> ServiceImpl<*>.ensureLoggedIn(block: () -> T): T {
    if (serviceContext.isLoggedIn() != ContextCheckResult.Allow) throw AccessDenied()
    return block()
}

/**
 * Ensures that the block runs only when the principal has the security officer role.
 *
 * @throws   AccessDenied  The principal does not have the security officer role.
 */
fun <T> ServiceImpl<*>.ensureSecurityOfficer(block: () -> T): T {
    ensure(securityOfficerRole)
    return block()
}

/**
 * Ensures that the principal has the security officer role.
 *
 * @throws   AccessDenied  The principal does not have the security officer role.
 */
fun ServiceImpl<*>.ensureSecurityOfficer() {
    ensure(securityOfficerRole)
}

/**
 * Ensures that the principal has the security officer role.
 *
 * @throws   AccessDenied  The principal does not have the security officer role.
 */
fun ServiceImpl<*>.ensureTechnicalAdmin() {
    ensure(securityOfficerRole) // TODO introduce a technial admin role
}

/**
 * Ensures that there is an principal in the context (Session.principal != null).
 *
 * @throws   AccessDenied  There is no principal in the context.
 */
fun ServiceImpl<*>.ensureLoggedIn() {
    if (serviceContext.isLoggedIn() != ContextCheckResult.Allow) throw AccessDenied()
}

/**
 * Ensures that the block runs only when there context contains **ALL**
 * of the specified roles.
 *
 * @throws   AccessDenied  At least one of the roles is not in the context.
 */
fun ServiceImpl<*>.ensure(vararg roles: Role) {
    if (serviceContext.hasAll(*roles) != ContextCheckResult.Allow) throw AccessDenied()
}

/**
 * Ensures that the block runs only when there context contains **ALL**
 * of the specified roles.
 *
 * @throws   AccessDenied  At least one of the roles is not in the context.
 */
fun <T> ServiceImpl<*>.ensure(vararg roles: Role, block: () -> T): T {
    if (serviceContext.hasAll(*roles) != ContextCheckResult.Allow) throw AccessDenied()
    return block()
}

/**
 * Ensures that the block runs only when there context contains **ANY**
 * of the specified roles.
 *
 * @throws   AccessDenied  None of the roles are in the context.
 */
fun <T> ServiceImpl<*>.ensureAny(vararg roles: Role, block: () -> T): T {
    if (serviceContext.hasAny(*roles) != ContextCheckResult.Allow) throw AccessDenied()
    return block()
}

/**
 * Ensures that the [result] is [ContextCheckResult.Allow].
 *
 * @throws   AccessDenied  [result] is [ContextCheckResult.Deny]
 */
fun ensure(result: ContextCheckResult) {
    if (result != ContextCheckResult.Allow) throw AccessDenied()
}

/**
 * Ensures that the block runs only when [result] is [ContextCheckResult.Allow].
 *
 * @throws   AccessDenied  [result] is [ContextCheckResult.Deny]
 */
fun <T> ensure(result: ContextCheckResult, block: () -> T): T {
    if (result != ContextCheckResult.Allow) throw AccessDenied()
    return block()
}

/**
 * Allows the call if [block] returns with true.
 *
 * @throws  AccessDenied  If [block] returns with false.
 */
fun ensuredBy(block: () -> Boolean) {
    if (! block()) throw AccessDenied()
}

/**
 * Allows the call without any security checks.
 */
inline fun <T> ensuredByLogic(@Suppress("UNUSED_PARAMETER") explanation: String, block: () -> T): T {
    return block()
}

/**
 * Marks the following code as secure by some logic.
 */
fun ensuredByLogic(@Suppress("UNUSED_PARAMETER") explanation: String) {
    // nothing to do here, this is just a marker
}

/**
 * Marks the following as publicly accessible.
 */
fun publicAccess() {
    // nothing to do here, this is just a marker
}

/**
 * Ensure that the service context runs in the name of the principal specified or
 * in the name of a security officer.
 */
fun ServiceImpl<*>.ensureSelfOrSecurityOfficer(principal: UUID<Principal>) {
    ensure(serviceContext.isPrincipal(principal) or serviceContext.has(securityOfficerRole))
}

/**
 * Ensure that this code runs on a test site. Calls [siteImpl] to check if the site
 * is a test site or not.
 *
 * @throws  AccessDenied
 */
suspend fun ServiceImpl<*>.ensureTest() {
    if (!siteImpl(serviceContext).isTest()) throw AccessDenied()
}