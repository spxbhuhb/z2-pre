package hu.simplexion.z2.history.ui

import hu.simplexion.z2.commons.i18n.LocalizedTextStore

object historyStrings : LocalizedTextStore() {

    val histories by "Naplók"
    val historiesSupport by histories.support("Biztonsági, technikai, hiba és üzleti szintű naplók.")

    val overview by "Áttekintés"
    val security by "Biztonsági"
    val technical by "Technikai"
    val error by "Hiba"
    val business by "Üzleti"

    val historyOverview by "Napló áttekintés"
    val securityHistory by "Biztonsági napló"
    val technicalHistory by "Technikai napló"
    val errorHistory by "Hibanapló"
    val businessHistory by "Üzleti napló"
}
