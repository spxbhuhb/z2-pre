package hu.simplexion.z2.auth

import hu.simplexion.z2.application.applicationSettings
import hu.simplexion.z2.application.model.ApplicationSettings
import hu.simplexion.z2.auth.impl.AuthAdminImpl.Companion.authAdminImpl
import hu.simplexion.z2.auth.impl.PrincipalImpl.Companion.principalImpl
import hu.simplexion.z2.auth.impl.RoleImpl.Companion.roleImpl
import hu.simplexion.z2.auth.impl.SessionImpl.Companion.sessionImpl
import hu.simplexion.z2.auth.model.Principal
import hu.simplexion.z2.auth.table.CredentialsTable.Companion.credentialsTable
import hu.simplexion.z2.auth.table.PrincipalTable.Companion.principalTable
import hu.simplexion.z2.auth.table.RoleGrantTable.Companion.roleGrantTable
import hu.simplexion.z2.auth.table.RoleGroupTable.Companion.roleGroupTable
import hu.simplexion.z2.auth.table.RoleTable.Companion.roleTable
import hu.simplexion.z2.auth.table.SessionTable.Companion.sessionTable
import hu.simplexion.z2.auth.util.*
import hu.simplexion.z2.exposed.implementations
import hu.simplexion.z2.exposed.isAllEmpty
import hu.simplexion.z2.exposed.tables
import hu.simplexion.z2.setting.dsl.setting
import hu.simplexion.z2.util.UUID
import org.jetbrains.exposed.sql.transactions.transaction

val securityOfficerUuid by setting<UUID<Principal>> { ApplicationSettings.SECURITY_OFFICER_UUID_KEY } default UUID("065ca04d-6409-7a05-8000-07efb14cd596")

fun authJvm() {

    val tables = tables(
        principalTable,
        credentialsTable,
        roleTable,
        roleGrantTable,
        roleGroupTable,
        sessionTable
    )

    val firstTimeInit = isAllEmpty(*tables)

    val securityOfficerName = setting<String> { ApplicationSettings.SECURITY_OFFICER_NAME_KEY }.valueOrNull ?: "so"

    val anonymousUuid = setting<UUID<Principal>> { ApplicationSettings.ANONYMOUS_UUID_KEY }.valueOrNull ?: UUID("065cc983-47fb-7530-8000-4a6a6c4c8022")
    val anonymousName = setting<String> { ApplicationSettings.ANONYMOUS_NAME_KEY }.valueOrNull ?: "anonymous"

    transaction {
        with(applicationSettings) {
            if (firstTimeInit) {
                makePrincipal(securityOfficerUuid, securityOfficerName, password = securityOfficerName)

                makeRole(securityOfficerRole)
                makeRoleGrant(securityOfficerRole.uuid, securityOfficerUuid)

                makeRole(technicalAdminRole)
                makeRoleGrant(technicalAdminRole.uuid, securityOfficerUuid)

                makePrincipal(anonymousUuid, anonymousName)
            } else {
                validatePrincipal(securityOfficerUuid)

                validateRole(securityOfficerRole)
                validateRoleGrant(securityOfficerRole.uuid, securityOfficerUuid)

                validateRole(technicalAdminRole)
                // technical admin may be revoked from the security officer

                validatePrincipal(anonymousUuid, locked = true)
            }
        }
    }

    implementations(
        principalImpl,
        roleImpl,
        sessionImpl,
        authAdminImpl
    )
}
