package hu.simplexion.z2.worker.table

import hu.simplexion.z2.commons.util.UUID
import hu.simplexion.z2.exposed.SchematicUuidTable
import hu.simplexion.z2.exposed.jvm
import hu.simplexion.z2.worker.model.WorkerRegistration
import hu.simplexion.z2.worker.model.WorkerStartMode
import hu.simplexion.z2.worker.model.WorkerStatus
import kotlinx.datetime.Clock.System.now
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
    val startMode = enumerationByName<WorkerStartMode>("start_mode", 20)
    val autoStartDelay = duration("auto_start_delay")
    val lastStatusChange = timestamp("last_statusChange")
    val lastStatusMessage = text("last_status_message")
    val restartAfterError = bool("restart_after_error")
    val restartCount = integer("restart_count")
    val restartLimit = integer("restart_limit")
    val restartCountClearRuntime = duration("restart_count_clear_runtime")
    val restartDelay = duration("restart_delay")

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