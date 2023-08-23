package hu.simplexion.z2.auth.ui.account

import hu.simplexion.z2.auth.model.AccountPrivate
import hu.simplexion.z2.auth.ui.Accounts
import hu.simplexion.z2.auth.ui.authStrings
import hu.simplexion.z2.browser.css.backgroundTransparent
import hu.simplexion.z2.browser.css.borderOutline
import hu.simplexion.z2.browser.css.p0
import hu.simplexion.z2.browser.html.Z2
import hu.simplexion.z2.browser.layout.container
import hu.simplexion.z2.browser.layout.lowest
import hu.simplexion.z2.browser.material.basicStrings
import hu.simplexion.z2.browser.table.schematicColumn
import hu.simplexion.z2.browser.table.table

internal fun Z2.list() =
    lowest(borderOutline) {
        container(p0, backgroundTransparent, scroll = false) {

            table<AccountPrivate> {

                title {
                    text = authStrings.accounts
                    action(authStrings.addAccount) { add() }
                }

                rowId = { it.uuid }
                query = { Accounts.list() }

                schematicColumn { AccountPrivate().email }
                schematicColumn { AccountPrivate().fullName }
                schematicColumn { AccountPrivate().phone }

                actionColumn {
                    action {
                        label = basicStrings.edit
                        handler = { edit(it) }
                    }
                }

            }
        }
    }
