package hu.simplexion.z2.worker.ui

import hu.simplexion.z2.commons.i18n.LocalizedTextStore
import hu.simplexion.z2.commons.util.UUID

@Suppress("ClassName")
object workerStrings : LocalizedTextStore(UUID("885e18c7-cb5c-4e50-842f-58bd34ee895b")) {
    val removeWorker by "Remove Worker"
    val workers by "Workers"
    val worker by "Worker"
    val addWorker by "Add Worker"
    val editWorker by "Edit Worker"
    val missingProvider by "Cannot start worker, provider is missing"
}
