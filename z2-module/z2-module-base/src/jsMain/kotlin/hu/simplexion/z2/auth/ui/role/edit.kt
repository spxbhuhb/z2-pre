package hu.simplexion.z2.auth.ui.role

import hu.simplexion.z2.auth.model.Role
import hu.simplexion.z2.auth.roleService
import hu.simplexion.z2.auth.ui.authStrings
import hu.simplexion.z2.browser.browserStrings
import hu.simplexion.z2.browser.css.gridGap24
import hu.simplexion.z2.browser.css.p24
import hu.simplexion.z2.browser.css.w400
import hu.simplexion.z2.browser.html.grid
import hu.simplexion.z2.browser.material.button.textButton
import hu.simplexion.z2.browser.material.modal.modal
import hu.simplexion.z2.browser.nonmaterial.schematic.field
import hu.simplexion.z2.commons.i18n.LocalizedText
import hu.simplexion.z2.commons.util.localLaunch

internal fun add() =
    roleModal(Role(), authStrings.addRole, browserStrings.add) { roleService.add(it) }

internal fun edit(role: Role) =
    roleModal(role, authStrings.editRole, browserStrings.edit) { roleService.update(it) }

internal fun roleModal(
    role: Role,
    modalTitle: LocalizedText,
    buttonLabel: LocalizedText,
    onOk: suspend (role: Role) -> Unit
) {
    localLaunch {
        modal(w400) {
            title(modalTitle)

            grid(p24, gridGap24) {
                field { role.programmaticName }
                field { role.displayName }
                field { role.contextName }
                field { role.displayOrder }
            }

            buttons {
                textButton(browserStrings.cancel) { closeWith(false) }
                textButton(buttonLabel) {
                    localLaunch {
                        onOk(role)
                        closeWith(true)
                    }
                }
            }
        }.show()
    }
}