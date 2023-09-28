package hu.simplexion.z2.auth.ui.role

import hu.simplexion.z2.auth.model.Role
import hu.simplexion.z2.auth.roleService
import hu.simplexion.z2.auth.ui.authStrings
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

            table<Role> {

                title {
                    text = authStrings.roles
                    action(authStrings.addRole) { add() }
                }

                rowId = { it.uuid }
                query = { roleService.list() }

                schematicColumn { Role().programmaticName }
                schematicColumn { Role().displayName }
                schematicColumn { Role().contextName }
                schematicColumn { Role().displayOrder }

                actionColumn {
                    action {
                        label = browserStrings.edit
                        handler = { edit(it) }
                    }
                }

            }
        }
    }
