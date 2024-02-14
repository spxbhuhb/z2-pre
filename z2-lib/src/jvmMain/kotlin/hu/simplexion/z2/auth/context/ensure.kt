package hu.simplexion.z2.auth.context

import hu.simplexion.z2.application.ApplicationSettings
import hu.simplexion.z2.application.ApplicationSettings.securityOfficerRoleUuid
import hu.simplexion.z2.application.ApplicationSettings.technicalAdminRoleUuid
import hu.simplexion.z2.auth.model.Principal
import hu.simplexion.z2.auth.model.Role
import hu.simplexion.z2.services.ServiceImpl
import hu.simplexion.z2.site.impl.SiteImpl.Companion.siteImpl
import hu.simplexion.z2.util.UUID

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
 * Ensures that the principal has the security officer role.
 *
 * @throws   AccessDenied  The principal does not have the security officer role.
 */
fun ServiceImpl<*>.ensureSecurityOfficer() {
    ensureHas(securityOfficerRoleUuid)
}

/**
 * Ensures that the principal has one of the roles listed in [technicalAdminRoles].
 *
 * @throws   AccessDenied  The principal does not have any of the roles listed in [technicalAdminRoles].
 */
fun ServiceImpl<*>.ensureTechnicalAdmin() {
    ensureHas(technicalAdminRoleUuid)
}

/**
 * Ensures that the principal has the security officer role.
 *
 * @throws   AccessDenied  The principal does not have the security officer role.
 */
fun ServiceImpl<*>.ensureSecurityOfficerOrInternal() {
    if (serviceContext.isInternal() == ContextCheckResult.Allow) return
    ensureSecurityOfficer()
}

/**
 * Ensures that there is a principal in the context (Session.principal != null).
 *
 * @throws   AccessDenied  There is no principal in the context.
 */
fun ServiceImpl<*>.ensureLoggedIn() {
    if (serviceContext.isLoggedIn() != ContextCheckResult.Allow) throw AccessDenied()
}

/**
 * Ensures that the context has the specified role.
 *
 * @throws   AccessDenied  At least one of the roles is not in the context.
 */
fun ServiceImpl<*>.ensureHas(roleUuid: UUID<Role>) {
    if (serviceContext.has(roleUuid) != ContextCheckResult.Allow) throw AccessDenied()
}

/**
 * Ensures that the block runs only when there context contains **ALL**
 * of the specified roles.
 *
 * @throws   AccessDenied  At least one of the roles is not in the context.
 */
fun ServiceImpl<*>.ensureAll(vararg roles: Role) {
    if (serviceContext.hasAll(*roles) != ContextCheckResult.Allow) throw AccessDenied()
}

/**
 * Ensures that the block runs only when there context contains **ALL**
 * of the specified roles.
 *
 * @throws   AccessDenied  At least one of the roles is not in the context.
 */
fun ServiceImpl<*>.ensureAll(vararg roles: UUID<Role>) {
    if (serviceContext.hasAll(*roles) != ContextCheckResult.Allow) throw AccessDenied()
}

/**
 * Ensures that the block runs only when there context contains **ANY**
 * of the specified roles.
 *
 * @throws   AccessDenied  None of the roles are in the context.
 */
fun ServiceImpl<*>.ensureAny(vararg roles: Role) {
    if (serviceContext.hasAny(*roles) != ContextCheckResult.Allow) throw AccessDenied()
}

/**
 * Ensures that the block runs only when there context contains **ANY**
 * of the specified roles.
 *
 * @throws   AccessDenied  None of the roles are in the context.
 */
fun ServiceImpl<*>.ensureAny(vararg roleUuids: UUID<Role>) {
    if (serviceContext.hasAny(*roleUuids) != ContextCheckResult.Allow) throw AccessDenied()
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
 * Allows the call if [block] returns with true.
 *
 * @throws  AccessDenied  If [block] returns with false.
 */
fun ensuredBy(block: () -> Boolean) {
    if (! block()) throw AccessDenied()
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
 * Ensure that the service context runs in the name of the principal specified.
 */
fun ServiceImpl<*>.ensureSelf(principal: UUID<Principal>) {
    ensure(serviceContext.isPrincipal(principal))
}

/**
 * Ensure that the service context runs in the name of the principal specified or
 * in the name of a security officer.
 */
fun ServiceImpl<*>.ensureSelfOrSecurityOfficer(principal: UUID<Principal>) {
    ensure(serviceContext.isPrincipal(principal) or serviceContext.has(ApplicationSettings.securityOfficerRoleUuid))
}

/**
 * Ensure that the service context runs in the name of the principal specified or
 * in the name of technical admin.
 */
fun ServiceImpl<*>.ensureSelfOrTechnicalAdmin(principal: UUID<Principal>) {
    ensure(serviceContext.isPrincipal(principal) or serviceContext.has(technicalAdminRoleUuid))
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

/**
 * Ensure that this code fails with NotImplementedError.
 *
 * @throws  AccessDenied
 */
fun ensureFailNotImplemented(message : () -> String) {
    throw NotImplementedError(message())
}