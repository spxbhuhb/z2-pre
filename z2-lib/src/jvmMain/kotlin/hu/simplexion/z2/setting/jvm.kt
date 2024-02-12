package hu.simplexion.z2.setting

import hu.simplexion.z2.exposed.implementations
import hu.simplexion.z2.exposed.tables
import hu.simplexion.z2.setting.implementation.SettingImpl
import hu.simplexion.z2.setting.persistence.SettingTable.Companion.settingTable

val settingImpl = SettingImpl()

fun settingJvm() {
    settingCommon()
    tables(settingTable)
    implementations(settingImpl)
}