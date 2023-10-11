package hu.simplexion.z2.logging.util

import hu.simplexion.z2.commons.localization.format
import hu.simplexion.z2.commons.localization.locales.localized
import hu.simplexion.z2.commons.localization.text.LocalizedText
import hu.simplexion.z2.commons.util.hereAndNow

fun info(topic : LocalizedText, message : LocalizedText) {
    println("${hereAndNow().localized}  [${topic.toString().padEnd(30).substring(0, 30)}]  $message")
}

fun info(topic : LocalizedText, message : LocalizedText, vararg parameters: Pair<LocalizedText, Any?>) {
    println("${hereAndNow().localized}  [${topic.toString().padEnd(30).substring(0, 30)}]  $message  ${parameters.format()}")
}
