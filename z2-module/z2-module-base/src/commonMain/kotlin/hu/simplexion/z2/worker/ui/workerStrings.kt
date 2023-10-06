package hu.simplexion.z2.worker.ui

import hu.simplexion.z2.commons.localization.text.LocalizedTextProvider

object workerStrings : IWorkerStrings

interface IWorkerStrings : LocalizedTextProvider {
    val removeWorker get() = static("Remove Worker")
    val workers get() = static("Workers")
    val worker get() = static("Worker")
    val addWorker get() = static("Add Worker")
    val editWorker get() = static("Edit Worker")
    val missingProvider get() = static("Cannot start worker, provider is missing")
    val unexpectedError get() = static("unexpected error")
    val statusChange get() = static("Worker Status Change")
    val startRuntime get() = static("starting worker runtime and all auto-start workers")
    val stopRuntime get() = static("stopping all workers and the worker runtime")
    val addProvider get() = static("add worker provider")
    val provider get() = static("provider")
    val setStoppedDuringStart get() = static("Worker status set to stopped because it cannot be running during startup. This may happen if the system had an uncontrolled shutdown.")
}