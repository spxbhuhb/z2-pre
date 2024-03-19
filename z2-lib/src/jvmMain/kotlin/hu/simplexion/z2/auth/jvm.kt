package hu.simplexion.z2.auth

import hu.simplexion.z2.application.ApplicationSettings
import hu.simplexion.z2.auth.impl.AuthAdminImpl.Companion.authAdminImpl
import hu.simplexion.z2.auth.impl.PrincipalImpl.Companion.principalImpl
import hu.simplexion.z2.auth.impl.RoleImpl.Companion.roleImpl
import hu.simplexion.z2.auth.impl.SessionImpl.Companion.sessionImpl
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
import org.jetbrains.exposed.sql.transactions.transaction

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

    transaction {
        with(ApplicationSettings) {
            if (firstTimeInit) {
                makePrincipal(securityOfficerUuid, securityOfficerName, password = securityOfficerName)

                makeRole(securityOfficerRole)
                makeRoleGrant(securityOfficerRoleUuid, securityOfficerUuid)

                makeRole(technicalAdminRole)
                makeRoleGrant(securityOfficerRoleUuid, securityOfficerUuid)

                makePrincipal(anonymousUuid, anonymousName)
            } else {
                validatePrincipal(securityOfficerUuid)

                validateRole(securityOfficerRole)
                validateRoleGrant(securityOfficerRoleUuid, securityOfficerUuid)

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
