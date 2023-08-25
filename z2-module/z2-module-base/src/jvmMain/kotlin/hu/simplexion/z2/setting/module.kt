package hu.simplexion.z2.setting

import hu.simplexion.z2.exposed.implementations
import hu.simplexion.z2.exposed.tables
import hu.simplexion.z2.setting.impl.SettingImpl
import hu.simplexion.z2.setting.impl.SettingImpl.Companion.settingImpl
import hu.simplexion.z2.setting.tables.SettingTable
import hu.simplexion.z2.setting.tables.SettingTable.Companion.settingTable

fun settingJvm() {
    settingCommon()
    tables(settingTable)
    implementations(settingImpl)
}