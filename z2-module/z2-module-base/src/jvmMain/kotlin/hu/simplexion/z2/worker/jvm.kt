package hu.simplexion.z2.worker

import hu.simplexion.z2.exposed.implementations
import hu.simplexion.z2.exposed.tables
import hu.simplexion.z2.setting.impl.SettingImpl.Companion.settingImpl
import hu.simplexion.z2.setting.table.SettingTable.Companion.settingTable
import hu.simplexion.z2.worker.impl.WorkerImpl.Companion.workerImpl
import hu.simplexion.z2.worker.table.WorkerRegistrationTable.Companion.workerRegistrationTable

fun workerJvm() {
    workerCommon()
    tables(workerRegistrationTable)
    implementations(workerImpl)
}