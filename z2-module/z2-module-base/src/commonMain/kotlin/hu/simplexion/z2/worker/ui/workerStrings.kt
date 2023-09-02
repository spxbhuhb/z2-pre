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
    val unexpectedError by "unexpected error"
    val statusChange by "Worker Status Change"
    val startRuntime by "starting worker runtime and all auto-start workers"
    val stopRuntime by "stopping all workers and the worker runtime"
    val addProvider by "add worker provider"
    val provider by "provider"
    val setStoppedDuringStart by "Worker status set to stopped because it cannot be running during startup. This may happen if the system had an uncontrolled shutdown."
}
