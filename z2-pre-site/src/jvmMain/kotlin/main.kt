import hu.simplexion.z2.application.bootJvm
import hu.simplexion.z2.baseStrings
import hu.simplexion.z2.browser.browserStrings
import hu.simplexion.z2.browser.demo.strings
import hu.simplexion.z2.exposed.JdbcSettings
import hu.simplexion.z2.localization.text.commonStrings
import hu.simplexion.z2.localization.text.dateTimeStrings
import hu.simplexion.z2.schematic.schema.validation.validationStrings
import hu.simplexion.z2.setting.dsl.settingsFromEnvironment
import hu.simplexion.z2.setting.dsl.settingsFromPropertyFile
import hu.simplexion.z2.setting.dsl.settingsFromSqlTable
import hu.simplexion.z2.setting.persistence.SettingTable.Companion.settingTable

fun main() {

    settingsFromEnvironment { "Z2_" }
    settingsFromPropertyFile { "../app/etc/z2.properties" }

    JdbcSettings().connect()

    settingsFromSqlTable { settingTable }

    commonStrings = strings
    dateTimeStrings = strings
    browserStrings = strings
    baseStrings = strings
    validationStrings = strings

    bootJvm({}) {
        init()
    }
}