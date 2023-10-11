package hu.simplexion.z2.worker.model

import hu.simplexion.z2.schematic.Schematic

class WorkerRegistration : Schematic<WorkerRegistration>() {

    var uuid by uuid<WorkerRegistration>()

    var provider by uuid<WorkerProvider>()

    var name by string() maxLength 100

    var enabled by boolean()

    var status by enum<WorkerStatus>()

    var startMode by enum<WorkerStartMode>()

    /**
     * When [startMode] is [WorkerStartMode.Automatic] the system waits for [autoStartDelay] before
     * it actually starts the worker.
     */
    var autoStartDelay by duration()

    /**
     * Last time the status of this worker has been changed.
     */
    var lastStatusChange by instant()

    /**
     * An optional message associated with the last status change of this worker.
     */
    var lastStatusMessage by string()

    /**
     * When true, the system restarts the worker after an unexpected error. When false, it stops after such errors.
     * [restartLimit] sets how many times the automatic restart may happen.
     */
    var restartAfterError by boolean()

    /**
     * The number of restarts that happened automatically because of an unexpected error. When this
     * reaches [restartLimit], the system sets the status of the worker to [WorkerStatus.Fault].
     */
    var restartCount by int()

    /**
     * Specifies how many times the worker may restart after an error. This may be used to prevent
     * the worker from restarting infinitely.
     */
    var restartLimit by int() default 1

    /**
     * The system set [restartCount] to zero if the worker runs for this duration without an unexpected error.
     */
    var restartCountClearRuntime by duration()

    /**
     * Specifies how much time to wait between automatic restarts.
     */
    var restartDelay by duration()

}