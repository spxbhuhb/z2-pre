package hu.simplexion.z2.auth.ui.role

import hu.simplexion.z2.auth.model.Role
import hu.simplexion.z2.auth.ui.Roles
import hu.simplexion.z2.auth.ui.strings
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

            table<Role> {

                title {
                    text = strings.roles
                    action(strings.addRole) { add() }
                }

                rowId = { it.id }
                query = { Roles.list() }

                schematicColumn { Role().programmaticName }
                schematicColumn { Role().displayName }
                schematicColumn { Role().contextName }

                actionColumn {
                    action {
                        label = basicStrings.edit
                        handler = { edit(it) }
                    }
                }

            }
        }
    }
