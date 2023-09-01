package hu.simplexion.z2.logging.util

import hu.simplexion.z2.commons.i18n.LocalizedText
import hu.simplexion.z2.commons.i18n.format
import hu.simplexion.z2.commons.util.hereAndNow
import hu.simplexion.z2.i18n.locales.localized

fun info(topic : LocalizedText, message : LocalizedText) {
    println("${hereAndNow().localized}  [${topic.toString().padEnd(30).substring(0, 30)}]  $message")
}

fun info(topic : LocalizedText, message : LocalizedText, vararg parameters: Pair<LocalizedText, Any?>) {
    println("${hereAndNow().localized}  [${topic.toString().padEnd(30).substring(0, 30)}]  $message  ${parameters.format()}")
}
