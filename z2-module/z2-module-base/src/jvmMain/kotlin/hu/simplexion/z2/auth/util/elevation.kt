package hu.simplexion.z2.auth.util

import hu.simplexion.z2.auth.model.Session
import hu.simplexion.z2.auth.securityOfficerAccountName
import hu.simplexion.z2.auth.securityOfficerRoleName
import hu.simplexion.z2.auth.securityOfficerUuid
import hu.simplexion.z2.service.runtime.BasicServiceContext
import hu.simplexion.z2.service.runtime.ServiceContext

fun <T> runAsSecurityOfficer(block: (context: ServiceContext) -> T): T {
    val context = BasicServiceContext()
    context.data[Session.SESSION_TOKEN_UUID] = Session().also {
        it.account = securityOfficerUuid
        it.fullName = securityOfficerAccountName
        it.roles = listOf(securityOfficerRoleName)
    }
    return block(context)
}