package hu.simplexion.z2.history

import hu.simplexion.z2.history.api.HistoryApi
import hu.simplexion.z2.service.runtime.getService

val histories = getService<HistoryApi>()

fun historyJs() {
    historyCommon()
}