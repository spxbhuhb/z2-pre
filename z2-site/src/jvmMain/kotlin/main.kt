import hu.simplexion.z2.baseStrings
import hu.simplexion.z2.browser.browserStrings
import hu.simplexion.z2.browser.demo.strings
import hu.simplexion.z2.exposed.JdbcSettings
import hu.simplexion.z2.localization.text.commonStrings
import hu.simplexion.z2.localization.text.dateTimeStrings
import hu.simplexion.z2.schematic.schema.validation.validationStrings
import hu.simplexion.z2.setting.dsl.settings
import hu.simplexion.z2.setting.persistence.SettingTable.Companion.settingTable
import hu.simplexion.z2.site.boot.bootJvm

fun main() {

    settings {
        environment { "Z2_" }
        propertyFile { "../app/etc/z2.properties" }
    }

    JdbcSettings().connect()

    settings {
        sql { settingTable }
    }

    commonStrings = strings
    dateTimeStrings = strings
    browserStrings = strings
    baseStrings = strings
    validationStrings = strings

    bootJvm({}) {
        init()
    }
}