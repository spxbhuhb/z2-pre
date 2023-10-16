import hu.simplexion.z2.baseStrings
import hu.simplexion.z2.browser.browserStrings
import hu.simplexion.z2.browser.demo.strings
import hu.simplexion.z2.localization.text.commonStrings
import hu.simplexion.z2.localization.text.dateTimeStrings
import hu.simplexion.z2.site.boot.bootJvm

fun main() {

    commonStrings = strings
    dateTimeStrings = strings
    browserStrings = strings
    baseStrings = strings

    bootJvm {
        init()
    }
}