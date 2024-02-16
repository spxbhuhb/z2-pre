package hu.simplexion.z2.auth.util

import hu.simplexion.z2.application.ApplicationSettings.securityOfficerRoleUuid
import hu.simplexion.z2.application.ApplicationSettings.securityOfficerUuid
import hu.simplexion.z2.application.ApplicationSettings.technicalAdminRoleUuid
import hu.simplexion.z2.auth.model.Session
import hu.simplexion.z2.services.BasicServiceContext
import hu.simplexion.z2.services.ServiceContext
import kotlinx.coroutines.runBlocking
import org.jetbrains.exposed.sql.transactions.transaction

suspend fun <T> runAsSecurityOfficer(block: suspend (context: ServiceContext) -> T): T {
    val context = BasicServiceContext()
    context.data[Session.SESSION_TOKEN_UUID] = Session().also {
        it.principal = securityOfficerUuid
        it.roles = listOf(securityOfficerRoleUuid)
    }
    return block(context)
}

fun <T> runTransactionAsSecurityOfficer(block: suspend (context: ServiceContext) -> T): T {
    return transaction { runBlockingAsSecurityOfficer { block(it) } }
}

fun <T> runBlockingAsSecurityOfficer(block: suspend (context: ServiceContext) -> T): T {
    return runBlocking { runAsSecurityOfficer { block(it) } }
}

suspend fun <T> runAsTechnicalAdmin(block: suspend (context: ServiceContext) -> T): T {
    val context = BasicServiceContext()
    context.data[Session.SESSION_TOKEN_UUID] = Session().also {
        it.principal = securityOfficerUuid
        it.roles = listOf(technicalAdminRoleUuid)
    }
    return block(context)
}

fun <T> runTransactionAsTechnicalAdmin(block: suspend (context: ServiceContext) -> T): T {
    return transaction { runBlockingAsTechnicalAdmin { block(it) } }
}

fun <T> runBlockingAsTechnicalAdmin(block: suspend (context: ServiceContext) -> T): T {
    return runBlocking { runAsTechnicalAdmin { block(it) } }
}