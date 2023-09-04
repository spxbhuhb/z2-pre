package hu.simplexion.z2.history.ui

import hu.simplexion.z2.browser.components.table.schematicColumn
import hu.simplexion.z2.browser.components.table.table
import hu.simplexion.z2.browser.css.backgroundTransparent
import hu.simplexion.z2.browser.css.borderOutline
import hu.simplexion.z2.browser.css.p0
import hu.simplexion.z2.browser.html.Z2
import hu.simplexion.z2.browser.layout.container
import hu.simplexion.z2.browser.layout.lowest
import hu.simplexion.z2.browser.material.basicStrings
import hu.simplexion.z2.history.histories
import hu.simplexion.z2.history.model.HistoryEntry
import hu.simplexion.z2.history.model.HistoryFlags
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.minus

internal fun Z2.list() =
    lowest(borderOutline) {

        container(p0, backgroundTransparent, scroll = false) {

            table<HistoryEntry> {

                title {
                    text = historyStrings.historyOverview
                }

                rowId = { it.uuid }
                query = { histories.list(HistoryFlags.ALL, oneDayBack, rightNow, 20).sortedByDescending { it.createdAt } }

                schematicColumn { HistoryEntry().createdAt }
                schematicColumn { HistoryEntry().createdBy }
                schematicColumn { HistoryEntry().textContent }

                actionColumn {
                    action {
                        label = basicStrings.details
                        handler = {  }
                    }
                }

            }
        }
    }

val rightNow
    get() = Clock.System.now()

val oneDayBack
    get() = Clock.System.now().minus(24, DateTimeUnit.HOUR)