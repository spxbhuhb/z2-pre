package hu.simplexion.z2.worker.ui

import hu.simplexion.z2.browser.html.Z2
import hu.simplexion.z2.worker.model.WorkerProvider

interface WorkerProviderJs : WorkerProvider {
    fun Z2.managementUi()
}