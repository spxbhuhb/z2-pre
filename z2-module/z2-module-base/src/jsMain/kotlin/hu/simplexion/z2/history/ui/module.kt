package hu.simplexion.z2.history.ui

import hu.simplexion.z2.history.api.HistoryApi
import hu.simplexion.z2.history.historyCommon
import hu.simplexion.z2.service.runtime.getService

val histories = getService<HistoryApi>()

fun history() {
    historyCommon()
}