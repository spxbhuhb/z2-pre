package hu.simplexion.z2.worker.table

import hu.simplexion.z2.commons.util.UUID
import hu.simplexion.z2.exposed.SchematicUuidTable
import hu.simplexion.z2.exposed.jvm
import hu.simplexion.z2.worker.model.WorkerRegistration
import hu.simplexion.z2.worker.model.WorkerStartMode
import hu.simplexion.z2.worker.model.WorkerStatus
import org.jetbrains.exposed.sql.kotlin.datetime.duration
import org.jetbrains.exposed.sql.kotlin.datetime.timestamp
import org.jetbrains.exposed.sql.update

open class WorkerRegistrationTable : SchematicUuidTable<WorkerRegistration>(
    "worker_registration",
    WorkerRegistration()
) {

    companion object {
        val workerRegistrationTable = WorkerRegistrationTable()
    }

    val provider = uuid("provider")
    val name = varchar("name", 100)
    val enabled = bool("enabled")
    val status = enumerationByName<WorkerStatus>("status", 20)
    val startMode = enumerationByName<WorkerStartMode>("startMode", 20)
    val autoStartDelay = duration("autoStartDelay")
    val lastStatusChange = timestamp("lastStatusChange")
    val lastStatusMessage = text("lastStatusMessage")
    val restartAfterError = bool("restartAfterError")
    val restartCount = integer("restartCount")
    val restartLimit = integer("restartLimit")
    val restartCountClearRuntime = duration("restartCountClearRuntime")
    val restartDelay = duration("restartDelay")

    fun setStatus(workerRegistration: WorkerRegistration) {
        update({ id eq workerRegistration.uuid.jvm }) {
            it[status] = workerRegistration.status
            it[lastStatusChange] = workerRegistration.lastStatusChange
            it[lastStatusMessage] = workerRegistration.lastStatusMessage
        }
    }

    fun setEnabled(inWorkerRegistration : UUID<WorkerRegistration>, inEnabled : Boolean) {
        update({ id eq inWorkerRegistration.jvm }) {
            it[enabled] = inEnabled
        }
    }
}