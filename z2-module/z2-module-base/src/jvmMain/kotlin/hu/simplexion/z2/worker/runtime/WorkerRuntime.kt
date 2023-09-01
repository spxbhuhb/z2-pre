package hu.simplexion.z2.worker.runtime

import hu.simplexion.z2.auth.context.account
import hu.simplexion.z2.auth.model.AccountPrivate
import hu.simplexion.z2.auth.util.runAsSecurityOfficer
import hu.simplexion.z2.commons.i18n.commonStrings
import hu.simplexion.z2.commons.util.UUID
import hu.simplexion.z2.history.util.systemHistory
import hu.simplexion.z2.history.util.technicalHistory
import hu.simplexion.z2.logging.util.info
import hu.simplexion.z2.worker.model.*
import hu.simplexion.z2.worker.table.WorkerRegistrationTable.Companion.workerRegistrationTable
import hu.simplexion.z2.worker.ui.workerStrings
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.datetime.Clock.System.now
import org.jetbrains.exposed.sql.transactions.transaction

open class WorkerRuntime {

    companion object {
        val workerRuntime = WorkerRuntime().also { it.start() }
    }

    protected val workerProviders = mutableMapOf<UUID<WorkerProvider>, WorkerProvider>()

    protected val workerRegistrations = mutableMapOf<UUID<WorkerRegistration>, WorkerRegistration>()

    protected val workerInstances = mutableMapOf<UUID<WorkerRegistration>, BackgroundWorker>()

    protected val scope = CoroutineScope(Dispatchers.IO)

    val messages = Channel<WorkerRuntimeRequest>(100)

    suspend fun sendAndWait(
        executor: UUID<AccountPrivate>,
        type: WorkerRuntimeMessageType,
        registration: WorkerRegistration? = null,
        registrationUuid: UUID<WorkerRegistration>? = null,
        provider: WorkerProvider? = null,
        timeout: Long = 3000L
    ): WorkerRuntimeResponse {
        val responseChannel = Channel<WorkerRuntimeResponse>(1)

        messages.send(
            WorkerRuntimeRequest(
                executor, type, registration, registrationUuid, provider, responseChannel
            )
        )

        return withTimeout(timeout) {
            val response = responseChannel.receive()
            if (response.exception != null) throw response.exception
            response
        }
    }

    operator fun plusAssign(provider: WorkerProvider) {
        runAsSecurityOfficer { context ->
            runBlocking {
                sendAndWait(context.account, WorkerRuntimeMessageType.AddProvider, null, null, provider)
            }
        }
    }

    open fun start() {
        info(workerStrings.workers, workerStrings.startRuntime)
        scope.launch { main() }
    }

    open fun stop(wait: Boolean = true) {
        runBlocking {
            info(workerStrings.workers, workerStrings.stopRuntime)
            messages.close()
            while (wait && scope.isActive) {
                delay(100)
            }
        }
    }

    protected suspend fun main() {
        transaction {
            for (registration in workerRegistrationTable.list()) {
                workerRegistrations[registration.uuid] = registration
                start(registration)
            }
        }

        try {

            for (message in messages) {
                try {
                    process(message)
                } catch (ex: Exception) {
                    message.responseChannel?.trySend(WorkerRuntimeResponse(exception = ex))
                }
            }

        } finally {
            scope.cancel()
            workerInstances.clear()
        }
    }

    protected suspend fun process(message: WorkerRuntimeRequest) {
        transaction {
            runBlocking {
                when (message.type) {
                    WorkerRuntimeMessageType.AddProvider -> addProvider(message)
                    WorkerRuntimeMessageType.AddRegistration -> addRegistration(message)
                    WorkerRuntimeMessageType.UpdateRegistration -> TODO()
                    WorkerRuntimeMessageType.RemoveRegistration -> removeRegistration(message)
                    WorkerRuntimeMessageType.ListRegistrations -> listRegistrations(message)
                    WorkerRuntimeMessageType.StartWorker -> startWorker(requireNotNull(message.registrationUuid))
                    WorkerRuntimeMessageType.StopWorker -> stopWorker(requireNotNull(message.registrationUuid))
                    WorkerRuntimeMessageType.EnableRegistration -> setEnabled(message, true)
                    WorkerRuntimeMessageType.DisableRegistration -> setEnabled(message, false)
                }
            }
        }
    }

    protected fun addProvider(message: WorkerRuntimeRequest) {
        val provider = requireNotNull(message.provider)
        require(provider.uuid !in workerProviders) { "attempt to register a provider twice: ${provider.uuid}" }
        workerProviders[provider.uuid] = provider
        message.responseChannel?.trySend(WorkerRuntimeResponse())

        info(workerStrings.worker, workerStrings.addProvider, workerStrings.provider to provider.uuid)
    }

    protected fun addRegistration(message: WorkerRuntimeRequest) {
        val registration = requireNotNull(message.registration)

        val uuid = workerRegistrationTable.insert(registration)
        val internal = registration.copy().also { it.uuid = uuid }

        technicalHistory(message.executor, workerStrings.worker, commonStrings.add, registration.uuid)

        workerRegistrations[uuid] = internal

        if (internal.enabled && internal.startMode != WorkerStartMode.Manual) {
            start(internal)
        }

        message.responseChannel?.trySend(WorkerRuntimeResponse(registrationUuid = uuid))
    }

    protected suspend fun updateRegistration(message: WorkerRuntimeRequest) {
        val update = requireNotNull(message.registration)
        val internal = requireNotNull(workerRegistrations[update.uuid])

        workerRegistrationTable.update(internal.uuid, internal)

        if (! internal.enabled) {
            stopWorker(internal.uuid)
        }

        technicalHistory(message.executor, workerStrings.worker, commonStrings.update, internal.uuid)

        message.responseChannel?.trySend(WorkerRuntimeResponse())
    }

    protected suspend fun removeRegistration(message: WorkerRuntimeRequest) {
        val uuid = requireNotNull(message.registrationUuid)

        technicalHistory(message.executor, workerStrings.worker, commonStrings.remove, uuid)

        workerRegistrations[uuid]?.also {
            stop(it)
            workerRegistrations.remove(uuid)
            workerRegistrationTable.remove(uuid)
        }

        message.responseChannel?.trySend(WorkerRuntimeResponse(registrationUuid = uuid))
    }

    protected suspend fun setEnabled(message: WorkerRuntimeRequest, enabled: Boolean) {
        val uuid = requireNotNull(message.registrationUuid)

        val registration = requireNotNull(workerRegistrations[uuid]) { "registration not found" }

        registration.enabled = enabled
        workerRegistrationTable.setEnabled(uuid, enabled)

        if (! enabled) {
            stopWorker(uuid)
        } else {
            if (registration.startMode != WorkerStartMode.Manual) {
                startWorker(uuid)
            }
        }

        technicalHistory(message.executor, workerStrings.worker, commonStrings.update, uuid, commonStrings.enabled to enabled)

        message.responseChannel?.trySend(WorkerRuntimeResponse(registrationUuid = uuid))
    }

    protected fun listRegistrations(message: WorkerRuntimeRequest) {
        message.responseChannel?.trySend(
            WorkerRuntimeResponse(
                registrationList = workerRegistrations.values.map { it.copy() }
            )
        )
    }

    protected fun startWorker(registration: UUID<WorkerRegistration>): Boolean {
        return start(requireNotNull(workerRegistrations[registration]))
    }

    protected fun start(registration: WorkerRegistration): Boolean {
        if (registration.uuid in workerInstances) return false

        val provider = workerProviders[registration.provider]
        check(provider != null) { workerStrings.missingProvider } // TODO documentation

        val instance = provider.newBackgroundWorker(registration)
        workerInstances[registration.uuid] = instance
        scope.launch { runWorker(instance) }
        return true
    }

    protected suspend fun stopWorker(registration: UUID<WorkerRegistration>) {
        stop(requireNotNull(workerRegistrations[registration]))
    }

    protected suspend fun stop(registration: WorkerRegistration) {
        // TODO think about the proper way to stop workers, maybe there is no proper way?
        // for sure the we should wait with other worker related operations until it
        // is stopping
        workerInstances.remove(registration.uuid)?.stop()
    }

    protected suspend fun runWorker(instance: BackgroundWorker) {
        val uuid = instance.registration
        uuid.setStatus(WorkerStatus.Running)

        try {
            instance.start()
            uuid.setStatus(WorkerStatus.Stopped)
        } catch (ex: Exception) {
            uuid.setStatus(WorkerStatus.Fault, ex.localizedMessage).also {
                // fIXME alarmImpl.alarm(it.uuid, workerStrings.unexpectedError, ex)
            }
        }
    }

    protected fun UUID<WorkerRegistration>.setStatus(inStatus: WorkerStatus, message: String = ""): WorkerRegistration {
        return workerRegistrations[this] !!.apply {
            status = inStatus
            lastStatusMessage = message
            lastStatusChange = now()
        }.also {
            transaction {
                workerRegistrationTable.setStatus(it)
                systemHistory(
                    workerStrings.worker,
                    commonStrings.update,
                    it.uuid,
                    commonStrings.name to it.name,
                    commonStrings.uuid to it.uuid,
                    commonStrings.status to it.status
                )
            }
        }
    }

}