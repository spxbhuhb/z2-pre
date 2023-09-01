package hu.simplexion.z2.email

import hu.simplexion.z2.auth.util.runAsSecurityOfficer
import hu.simplexion.z2.commons.util.nextHandle
import hu.simplexion.z2.email.impl.EmailImpl.Companion.emailImpl
import hu.simplexion.z2.email.table.EmailQueueTable.Companion.emailQueueTable
import hu.simplexion.z2.email.table.EmailTable.Companion.emailTable
import hu.simplexion.z2.email.worker.EmailWorkerProvider
import hu.simplexion.z2.exposed.implementations
import hu.simplexion.z2.exposed.tables
import hu.simplexion.z2.worker.impl.WorkerImpl.Companion.workerImpl
import hu.simplexion.z2.worker.runtime.WorkerRuntime.Companion.workerRuntime

internal val emailBusHandle = nextHandle()

fun emailJvm() {
    emailCommon()
    tables(emailTable, emailQueueTable)
    implementations(emailImpl)
    workerRuntime += EmailWorkerProvider()
}