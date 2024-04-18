package hu.simplexion.z2.history.api

import hu.simplexion.z2.history.model.HistoryEntry
import hu.simplexion.z2.services.Service
import kotlinx.datetime.Instant

interface HistoryApi : Service {

    suspend fun list(flags : Int, start : Instant, end : Instant, limit : Int): List<HistoryEntry>

}