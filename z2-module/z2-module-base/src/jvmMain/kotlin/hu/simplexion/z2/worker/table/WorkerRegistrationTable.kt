package hu.simplexion.z2.worker.table

import hu.simplexion.z2.commons.util.UUID
import hu.simplexion.z2.exposed.SchematicUuidTable
import hu.simplexion.z2.exposed.jvm
import hu.simplexion.z2.i18n.model.Language
import hu.simplexion.z2.worker.model.WorkerRegistration
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

    fun setEnabled(inWorkerRegistration : UUID<WorkerRegistration>, inEnabled : Boolean) {
        update({ id eq inWorkerRegistration.jvm }) {
            it[enabled] = inEnabled
        }
    }
}