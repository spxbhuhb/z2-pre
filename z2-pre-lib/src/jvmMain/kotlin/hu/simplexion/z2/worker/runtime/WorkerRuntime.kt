package hu.simplexion.z2.worker.runtime

import hu.simplexion.z2.auth.context.principal
import hu.simplexion.z2.auth.model.Principal
import hu.simplexion.z2.auth.util.runBlockingAsSecurityOfficer
import hu.simplexion.z2.baseStrings
import hu.simplexion.z2.util.UUID
import hu.simplexion.z2.history.util.technicalHistory
import hu.simplexion.z2.localization.text.commonStrings
import hu.simplexion.z2.worker.model.WorkerProvider
import hu.simplexion.z2.worker.model.WorkerRegistration
import hu.simplexion.z2.worker.model.WorkerStartMode
import hu.simplexion.z2.worker.model.WorkerStatus
import hu.simplexion.z2.worker.table.WorkerRegistrationTable.Companion.workerRegistrationTable
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.datetime.Clock.System.now
import org.jetbrains.exposed.sql.transactions.transaction
import kotlin.coroutines.coroutineContext

open class WorkerRuntime {

    companion object {
        val workerRuntime = WorkerRuntime()
    }

    protected val workerProviders = mutableMapOf<UUID<WorkerProvider>, WorkerProvider>()

    protected val workerRegistrations = mutableMapOf<UUID<WorkerRegistration>, WorkerRegistration>()

    protected val instancesMutex = Mutex()

    protected val workerInstances = mutableMapOf<UUID<WorkerRegistration>, WorkerInstance>()

    protected val scope = CoroutineScope(Dispatchers.IO)

    val messages = Channel<WorkerRuntimeRequest>(100)

    suspend fun sendAndWait(
        executor: UUID<Principal>,
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
        runBlockingAsSecurityOfficer { context ->
            sendAndWait(context.principal, WorkerRuntimeMessageType.AddProvider, null, null, provider)
        }
    }

    open fun start() {
        scope.launch { main() }
    }

    open fun stop(wait: Boolean = true) {
        runBlocking {
            messages.close()
            scope.cancel()
            while (wait && scope.isActive) {
                delay(100)
            }
        }
    }

    protected suspend fun main() {
        transaction {
            for (registration in workerRegistrationTable.list()) {
                workerRegistrations[registration.uuid] = registration
                if (registration.status != WorkerStatus.Stopped) {
                    registration.uuid.setStatus(WorkerStatus.Stopped, baseStrings.setStoppedDuringStart.toString())
                }
                if (registration.provider in workerProviders && registration.enabled && registration.startMode == WorkerStartMode.Automatic) {
                    start(registration)
                }
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
                    WorkerRuntimeMessageType.StartWorker -> start(requireNotNull(message.registrationUuid))
                    WorkerRuntimeMessageType.StopWorker -> stopWorker(requireNotNull(message.registrationUuid))
                    WorkerRuntimeMessageType.EnableRegistration -> setEnabled(message, true)
                    WorkerRuntimeMessageType.DisableRegistration -> setEnabled(message, false)
                }
            }
        }
    }

    protected fun addProvider(message: WorkerRuntimeRequest) {
        val provider = requireNotNull(message.provider)
        val existing = workerProviders[provider.uuid]
        if (existing != null) {
            require(provider::class == existing::class) {
                "attempt to register different providers with the same UUID: ${provider.uuid} ${provider::class.qualifiedName} ${existing::class.qualifiedName}"
            }
            return
        }
        workerProviders[provider.uuid] = provider

        for (registration in workerRegistrations.values) {
            if (registration.provider == provider.uuid && registration.status == WorkerStatus.Stopped && registration.startMode != WorkerStartMode.Manual) {
                start(registration)
            }
        }

        message.responseChannel?.trySend(WorkerRuntimeResponse())
    }

    protected fun addRegistration(message: WorkerRuntimeRequest) {
        val registration = requireNotNull(message.registration)

        val uuid = workerRegistrationTable.insert(registration)
        val internal = registration.copy().also { it.uuid = uuid }

        technicalHistory(message.executor, baseStrings.worker, commonStrings.add, registration.uuid)

        workerRegistrations[uuid] = internal

        if (internal.enabled && internal.startMode != WorkerStartMode.Manual && registration.provider in workerProviders) {
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

        technicalHistory(message.executor, baseStrings.worker, commonStrings.update, internal.uuid)

        message.responseChannel?.trySend(WorkerRuntimeResponse())
    }

    protected suspend fun removeRegistration(message: WorkerRuntimeRequest) {
        val uuid = requireNotNull(message.registrationUuid)

        technicalHistory(message.executor, baseStrings.worker, commonStrings.remove, uuid)

        workerRegistrations[uuid]?.also {
            stopWorker(uuid)
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
            if (registration.provider in workerProviders && registration.startMode != WorkerStartMode.Manual) {
                start(uuid)
            }
        }

        technicalHistory(message.executor, baseStrings.worker, commonStrings.update, uuid, commonStrings.enabled to enabled)

        message.responseChannel?.trySend(WorkerRuntimeResponse(registrationUuid = uuid))
    }

    protected fun listRegistrations(message: WorkerRuntimeRequest) {
        message.responseChannel?.trySend(
            WorkerRuntimeResponse(
                registrationList = workerRegistrations.values.map { it.copy() }
            )
        )
    }

    protected fun start(registration: UUID<WorkerRegistration>): Boolean {
        return start(requireNotNull(workerRegistrations[registration]))
    }

    protected fun start(registration: WorkerRegistration): Boolean {
        val provider = workerProviders[registration.provider]
        check(provider != null) { baseStrings.missingProvider }

        val instance = WorkerInstance(provider.newBackgroundWorker(registration))

        return runBlocking {
            instancesMutex.withLock {
                if (registration.uuid in workerInstances) {
                    false
                } else {
                    workerInstances[registration.uuid] = instance
                    scope.launch { runWorker(instance) }
                    while (! instance.hasJob()) {
                        delay(5)
                    }
                    true
                }
            }
        }
    }

    protected suspend fun stopWorker(registrationUuid: UUID<WorkerRegistration>) {
        val registration = requireNotNull(workerRegistrations[registrationUuid])
        workerInstances[registration.uuid]?.getJob()?.cancel()
    }

    /**
     * This method runs in a different coroutine context than the others, therefore
     * synchronization is necessary.
     */
    protected suspend fun runWorker(instance: WorkerInstance) {
        val job = coroutineContext[Job] !!
        instance.setJob(job)

        val uuid = instance.worker.registration
        uuid.setStatus(WorkerStatus.Running)

        try {
            instance.worker.run(job)
            uuid.setStatus(WorkerStatus.Stopped)
        } catch (ex: CancellationException) {
            uuid.setStatus(WorkerStatus.Stopped)
        } catch (ex: Exception) {
            uuid.setStatus(WorkerStatus.Fault, ex.stackTraceToString()).also {
                // fIXME alarmImpl.alarm(it.uuid, baseStrings.unexpectedError, ex)
                ex.printStackTrace()
            }
        } finally {
            instancesMutex.withLock {
                workerInstances.remove(instance.worker.registration)
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
                technicalHistory(
                    baseStrings.worker,
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