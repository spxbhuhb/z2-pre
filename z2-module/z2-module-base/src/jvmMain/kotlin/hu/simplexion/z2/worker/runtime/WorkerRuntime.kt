package hu.simplexion.z2.worker.runtime

import hu.simplexion.z2.alarm.impl.AlarmImpl.Companion.alarmImpl
import hu.simplexion.z2.auth.context.accountOrNull
import hu.simplexion.z2.commons.i18n.commonStrings
import hu.simplexion.z2.commons.util.Lock
import hu.simplexion.z2.commons.util.UUID
import hu.simplexion.z2.commons.util.use
import hu.simplexion.z2.history.ui.historyStrings.technicalHistory
import hu.simplexion.z2.history.util.systemHistory
import hu.simplexion.z2.history.util.technicalHistory
import hu.simplexion.z2.service.runtime.ServiceContext
import hu.simplexion.z2.worker.model.*
import hu.simplexion.z2.worker.table.WorkerRegistrationTable.Companion.workerRegistrationTable
import hu.simplexion.z2.worker.ui.workerStrings
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock.System.now

object WorkerRuntime {

    private val workerLock = Lock()

    private val workerProviders = mutableMapOf<UUID<WorkerProvider>, WorkerProvider>()

    private val workerRegistrations = mutableMapOf<UUID<WorkerRegistration>, WorkerRegistration>()

    private val workerInstances = mutableMapOf<UUID<WorkerRegistration>, BackgroundWorker>()

    private val workerScope = CoroutineScope(Dispatchers.IO)

    fun start() {
        for (registration in workerRegistrationTable.list()) {
            workerLock.use {
                workerRegistrations[registration.uuid] = registration
            }
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
            workerScope.launch { runWorker(instance) }
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

    suspend fun runWorker(instance: BackgroundWorker) {
        val uuid = instance.registration
        uuid.setStatus(WorkerStatus.Running)

        try {
            instance.start()
            uuid.setStatus(WorkerStatus.Stopped)
        } catch (ex: Exception) {
            uuid.setStatus(WorkerStatus.Fault, ex.localizedMessage).also {
                alarmImpl.alarm(it.uuid, workerStrings.unexpectedError, ex)
            }
        }
    }

    fun UUID<WorkerRegistration>.setStatus(inStatus: WorkerStatus, message: String = "") : WorkerRegistration {
        return workerLock.use {
            workerRegistrations[this] !!.apply {
                status = inStatus
                lastStatusMessage = message
                lastStatusChange = now()
            }
        }.also {
            workerRegistrationTable.setStatus(it)
            systemHistory(workerStrings.statusChange, it.uuid, commonStrings.name to it.name, commonStrings.uuid to it.uuid, commonStrings.status to it.status)
        }
    }

    // --------------------------------------------------------
    // API support
    // --------------------------------------------------------

    fun add(serviceContext: ServiceContext?, registration: WorkerRegistration): UUID<WorkerRegistration> {
        val uuid = workerRegistrationTable.insert(registration)
        technicalHistory(serviceContext, workerStrings.addWorker, uuid, commonStrings.data to registration)

        val internal = registration.copy().also { it.uuid = uuid }

        workerLock.use {
            workerRegistrations[uuid] = internal
            if (internal.enabled && internal.startMode != WorkerStartMode.Manual) {
                start(internal)
            }
        }

        return uuid
    }

    fun update(serviceContext: ServiceContext?, registration: WorkerRegistration) {
        val internal = workerLock.use {

        }

        technicalHistory(serviceContext, workerStrings.addWorker, uuid, commonStrings.data to registration)

    }

    fun remove(serviceContext: ServiceContext?, registration: UUID<WorkerRegistration>) {

    }

    fun list(): List<WorkerRegistration> {

    }

    fun setEnabled(serviceContext: ServiceContext?, registration: UUID<WorkerRegistration>, value : Boolean) {

    }


}