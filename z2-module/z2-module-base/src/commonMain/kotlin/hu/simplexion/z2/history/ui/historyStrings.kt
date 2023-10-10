package hu.simplexion.z2.history.ui

import hu.simplexion.z2.commons.localization.text.LocalizedTextProvider

object historyStrings : HistoryStrings

interface HistoryStrings : LocalizedTextProvider {

    val histories get() = static("Naplók", "Biztonsági, technikai, hiba és üzleti szintű naplók.")

    val overview get() = static("Áttekintés")
    val security get() = static("Biztonsági")
    val technical get() = static("Technikai")
    val error get() = static("Hiba")
    val business get() = static("Üzleti")

    val historyOverview get() = static("Napló áttekintés")
    val securityHistory get() = static("Biztonsági napló")
    val technicalHistory get() = static("Technikai napló")
    val errorHistory get() = static("Hibanapló")
    val businessHistory get() = static("Üzleti napló")
}
