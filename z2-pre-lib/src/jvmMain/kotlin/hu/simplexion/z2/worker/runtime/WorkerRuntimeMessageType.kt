package hu.simplexion.z2.worker.runtime

enum class WorkerRuntimeMessageType {
    AddProvider,
    AddRegistration,
    RemoveRegistration,
    UpdateRegistration,
    EnableRegistration,
    DisableRegistration,
    StartWorker,
    StopWorker,
    ListRegistrations
}