package hu.simplexion.z2.auth.ui.principal

import hu.simplexion.z2.auth.model.Principal
import hu.simplexion.z2.auth.principalService
import hu.simplexion.z2.baseStrings
import hu.simplexion.z2.browser.browserStrings
import hu.simplexion.z2.browser.css.backgroundTransparent
import hu.simplexion.z2.browser.css.borderOutline
import hu.simplexion.z2.browser.css.p0
import hu.simplexion.z2.browser.html.Z2
import hu.simplexion.z2.browser.immaterial.table.schematicColumn
import hu.simplexion.z2.browser.immaterial.table.table
import hu.simplexion.z2.browser.layout.surfaceContainer
import hu.simplexion.z2.browser.layout.surfaceContainerLowest

internal fun Z2.list() =
    surfaceContainerLowest(borderOutline) {
        surfaceContainer(p0, backgroundTransparent, scroll = false) {

            table<Principal> {

                title {
                    text = baseStrings.accounts
                    action(baseStrings.addAccount) { add() }
                }

                rowId = { it.uuid }
                query = { principalService.list() }

                with(Principal()) {
                    schematicColumn { name }
                    schematicColumn { locked }
                    schematicColumn { activated }
                    schematicColumn { expired }
                    schematicColumn { anonymized }
                    schematicColumn { lastAuthSuccess }
                    schematicColumn { lastAuthFail }
                    schematicColumn { authFailCount }
                }

                actionColumn {
                    action {
                        label = browserStrings.edit
                        handler = { edit(it) }
                    }
                }

            }
        }
    }
