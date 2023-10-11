package hu.simplexion.z2.auth.ui.account

import hu.simplexion.z2.auth.accountService
import hu.simplexion.z2.auth.model.AccountPrivate
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

            table<AccountPrivate> {

                title {
                    text = baseStrings.accounts
                    action(baseStrings.addAccount) { add() }
                }

                rowId = { it.uuid }
                query = { accountService.list() }

                schematicColumn { AccountPrivate().email }
                schematicColumn { AccountPrivate().fullName }
                schematicColumn { AccountPrivate().phone }

                actionColumn {
                    action {
                        label = browserStrings.edit
                        handler = { edit(it) }
                    }
                }

            }
        }
    }
