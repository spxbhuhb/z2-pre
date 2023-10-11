package hu.simplexion.z2.testing

import hu.simplexion.z2.auth.authJvm
import hu.simplexion.z2.auth.getOrMakeAccount
import hu.simplexion.z2.auth.impl.SessionImpl.Companion.sessionImpl
import hu.simplexion.z2.email.emailJvm
import hu.simplexion.z2.exposed.debugExposed
import hu.simplexion.z2.exposed.h2Test
import hu.simplexion.z2.history.historyJvm
import hu.simplexion.z2.service.BasicServiceContext
import hu.simplexion.z2.service.ServiceContext
import hu.simplexion.z2.setting.settingJvm
import hu.simplexion.z2.worker.workerJvm
import kotlinx.coroutines.runBlocking
import org.jetbrains.exposed.sql.transactions.transaction

fun integrated(
    login : Boolean = true,
    withTransaction : Boolean = true,
    debugExposed: Boolean = false,
    testFun: suspend (context: ServiceContext) -> Unit
) {
    runBlocking {
        debugExposed(debugExposed)
        h2Test()
        historyJvm()
        settingJvm()
        authJvm()
        workerJvm()
        emailJvm()

        getOrMakeAccount("test", "test", "test")

        val context = BasicServiceContext()

        if (login) {
            transaction {
                runBlocking { sessionImpl(context).login("test", "test")  }
            }
        }

        if (withTransaction) {
            transaction {
                runBlocking {
                    testFun(context)
                }
            }
        } else {
            runBlocking {
                testFun(context)
            }
        }
    }
}

fun integratedWithSo(
    login : Boolean = true,
    withTransaction : Boolean = true,
    debugExposed : Boolean = false,
    testFun: suspend (test: ServiceContext, so : ServiceContext) -> Unit
) {
    integrated(login, withTransaction, debugExposed) { test ->
        testFun(test, securityOfficerContext())
    }
}

fun securityOfficerContext() : ServiceContext {
    val context = BasicServiceContext()
    transaction {
        runBlocking { sessionImpl(context).login("so", "so")  }
    }
    return context
}

fun t(block: suspend () -> Unit) =
    transaction {
        runBlocking {
            block()
        }
    }