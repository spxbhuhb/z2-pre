package hu.simplexion.z2.logging.util

import hu.simplexion.z2.commons.util.hereAndNow
import hu.simplexion.z2.localization.format
import hu.simplexion.z2.localization.locales.localized
import hu.simplexion.z2.localization.text.LocalizedText

fun info(topic : LocalizedText, message : LocalizedText) {
    println("${hereAndNow().localized}  [${topic.toString().padEnd(30).substring(0, 30)}]  $message")
}

fun info(topic : LocalizedText, message : LocalizedText, vararg parameters: Pair<LocalizedText, Any?>) {
    println("${hereAndNow().localized}  [${topic.toString().padEnd(30).substring(0, 30)}]  $message  ${parameters.format()}")
}
