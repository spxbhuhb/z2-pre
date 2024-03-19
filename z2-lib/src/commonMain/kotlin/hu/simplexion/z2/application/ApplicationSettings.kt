package hu.simplexion.z2.application

import hu.simplexion.z2.auth.model.Principal
import hu.simplexion.z2.auth.model.Role
import hu.simplexion.z2.setting.dsl.setting
import hu.simplexion.z2.util.UUID

object ApplicationSettings {

    /**
     * The UUID that represents the application itself.
     *
     * - Should not be changed after the first time the application runs.
     * - [applicationUuid] is the owner of all application-wide settings.
     */
    val applicationUuid by setting<UUID<Principal>> { "APPLICATION_UUID" } default UUID("065ca04c-cd6d-7775-8000-5fa6142b4b7b")

    /**
     * The UUID of the built-in security officer user.
     *
     * - Should not be changed after the first time the application runs.
     * - The initialization sequence creates this principal if there are no principals at the time the application starts.
     * - If there are principals but this one is missing, the application stops.
     */
    val securityOfficerUuid by setting<UUID<Principal>> { "SECURITY_OFFICER_UUID" } default UUID("065ca04d-6409-7a05-8000-07efb14cd596")

    /**
     * The principal name of the built-in security officer user.
     *
     * - Used during first time application startup to create the principal of the security officer.
     */
    val securityOfficerName by setting<String> { "SECURITY_OFFICER_NAME" } default "so"

    /**
     * The UUID of the built-in security officer role.
     *
     * - Should not be changed after the first time the application runs.
     * - The initialization sequence creates this role if there are no roles at the time the application starts.
     * - If there are roles but this one is missing, the application stops.
     */
    val securityOfficerRoleUuid by setting<UUID<Role>> { "SECURITY_OFFICER_ROLE_UUID" } default UUID("065ca04e-095c-713b-8000-beb6c07f18cd")

    /**
     * The programmatic name of the built-in security officer role.
     *
     * - Used during first time application startup to create the role of the security officer.
     */
    val securityOfficerRoleName by setting<String> { "SECURITY_OFFICER_ROLE_NAME" } default "security-officer"

    /**
     * The built-in security officer role. Initialized from [securityOfficerRoleName] and [securityOfficerRoleUuid].
     */
    val securityOfficerRole by lazy {
        Role().also {
            it.uuid = securityOfficerRoleUuid
            it.programmaticName = securityOfficerRoleName
            it.displayName = securityOfficerRoleName
        }
    }

    /**
     * The UUID of the built-in technical administrator role.
     *
     * - Should not be changed after the first time the application runs.
     * - The initialization sequence creates this role if there are no roles at the time the application starts.
     * - If there are roles but this one is missing, the application stops.
     */
    val technicalAdminRoleUuid by setting<UUID<Role>> { "TECHNICAL_ADMIN_ROLE_UUID" } default UUID("065cca1e-9785-79b6-8000-143a52a8d7e3")

    /**
     * The programmatic name of the built-in security officer role.
     *
     * - Used during first time application startup to create the role of the security officer.
     */
    val technicalAdminRoleName by setting<String> { "TECHNICAL_ADMIN_ROLE_NAME" } default "technical-admin"

    /**
     * The built-in technical admin role. Fields are initialized from [technicalAdminRoleName]
     * and [technicalAdminRoleUuid].
     */
    val technicalAdminRole by lazy {
        Role().also {
            it.uuid = technicalAdminRoleUuid
            it.programmaticName = technicalAdminRoleName
            it.displayName = technicalAdminRoleName
        }
    }

    /**
     * The UUID of the built-in security officer user.
     *
     * - Should not be changed after the first time the application runs.
     * - The initialization sequence creates this principal if there are no principals at the time the application starts.
     * - If there are principals but this one is missing, the application stops.
     */
    val anonymousUuid by setting<UUID<Principal>> { "ANONYMOUS_UUID" } default UUID("065cc983-47fb-7530-8000-4a6a6c4c8022")

    /**
     * The principal name of the built-in anonymous user.
     *
     * - Used during first time application startup to create the principal of the anonymous user.
     */
    val anonymousName by setting<String> { "ANONYMOUS_NAME" } default "anonymous"

}