package hu.simplexion.z2.worker.runtime

import hu.simplexion.z2.commons.i18n.commonStrings
import hu.simplexion.z2.commons.util.Lock
import hu.simplexion.z2.commons.util.UUID
import hu.simplexion.z2.commons.util.use
import hu.simplexion.z2.history.util.systemHistory
import hu.simplexion.z2.worker.model.BackgroundWorker
import hu.simplexion.z2.worker.model.WorkerProvider
import hu.simplexion.z2.worker.model.WorkerRegistration
import hu.simplexion.z2.worker.table.WorkerRegistrationTable
import hu.simplexion.z2.worker.ui.workerStrings
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

object WorkerRuntime {

    private val workerLock = Lock()

    private val workerProviders = mutableMapOf<UUID<WorkerProvider>, WorkerProvider>()

    private val workerRegistrations = mutableMapOf<UUID<WorkerRegistration>, WorkerRegistration>()

    private val workerInstances = mutableMapOf<UUID<WorkerRegistration>, BackgroundWorker>()

    private val workerScope = CoroutineScope(Dispatchers.Default)

    fun start() {
        for (registration in WorkerRegistrationTable.workerRegistrationTable.list()) {
            workerLock.use {
                workerRegistrations[registration.uuid] = registration
            }
            systemHistory(workerStrings.workers, commonStrings.start, registration)
            start(registration)
        }
    }

    fun stop() {
        workerLock.use {
            workerScope.cancel()
            workerInstances.clear()
        }
    }

    operator fun plusAssign(registration: WorkerRegistration) {
        workerLock.use {
            require(registration.uuid !in workerRegistrations)
            workerRegistrations[registration.uuid] = registration
            if (registration.enabled) {
                start(registration)
            }
        }
    }

    suspend operator fun minusAssign(registration: UUID<WorkerRegistration>) {
        workerLock.use {
            requireNotNull(workerRegistrations[registration]).also {
                stop(it)
                workerRegistrations.remove(registration)
            }
        }
    }

    operator fun plusAssign(provider: WorkerProvider) {
        workerProviders[provider.uuid] = provider
    }

    internal fun start(registration: UUID<WorkerRegistration>): Boolean {
        return start(requireNotNull(workerRegistrations[registration]))
    }

    internal fun start(registration: WorkerRegistration): Boolean {
        workerLock.use {
            if (registration.uuid in workerInstances) return false

            val provider = workerProviders[registration.provider]
            check(provider != null) { workerStrings.missingProvider } // TODO documentation

            val instance = provider.newBackgroundWorker(registration)
            workerInstances[registration.uuid] = instance
            workerScope.launch { instance.start() }
            return true
        }
    }

    internal suspend fun stop(registration: UUID<WorkerRegistration>) {
        stop(requireNotNull(workerRegistrations[registration]))
    }

    internal suspend fun stop(registration: WorkerRegistration) {
        workerLock.use {
            // TODO think about the proper way to stop workers, maybe there is no proper way?
            // for sure the we should wait with other worker related operations until it
            // is stopping
            workerInstances.remove(registration.uuid)?.stop()
        }
    }

}