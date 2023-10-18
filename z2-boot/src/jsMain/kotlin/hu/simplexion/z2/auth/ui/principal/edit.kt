package hu.simplexion.z2.auth.ui.principal

import hu.simplexion.z2.auth.model.Principal
import hu.simplexion.z2.auth.principalService
import hu.simplexion.z2.baseStrings
import hu.simplexion.z2.browser.browserStrings
import hu.simplexion.z2.browser.css.gridGap24
import hu.simplexion.z2.browser.css.p24
import hu.simplexion.z2.browser.css.w400
import hu.simplexion.z2.browser.html.grid
import hu.simplexion.z2.browser.immaterial.schematic.field
import hu.simplexion.z2.browser.material.button.textButton
import hu.simplexion.z2.browser.material.modal.modal
import hu.simplexion.z2.commons.util.localLaunch
import hu.simplexion.z2.commons.util.randomBase64String256Bit
import hu.simplexion.z2.localization.text.LocalizedText

internal fun add() =
    accountModal(Principal(), baseStrings.addAccount, browserStrings.add) {
        principalService.add(it, false, randomBase64String256Bit(), emptyList())
    }

internal fun edit(role: Principal) =
    accountModal(role, baseStrings.editRole, browserStrings.add) {  }

internal fun accountModal(
    principal: Principal,
    modalTitle: LocalizedText,
    buttonLabel: LocalizedText,
    onOk: suspend (role: Principal) -> Unit
) {
    localLaunch {
        modal(w400) {
            title(modalTitle)

            grid(p24, gridGap24) {
                with(Principal()) {
                    field { name }
                    field { locked }
                    field { activated }
                    field { expired }
                    field { anonymized }
                    field { lastAuthSuccess }
                    field { lastAuthFail }
                    field { authFailCount }
                }
            }

            buttons {
                textButton(browserStrings.cancel) { closeWith(false) }
                textButton(buttonLabel) {
                    localLaunch {
                        onOk(principal)
                        closeWith(true)
                    }
                }
            }
        }.show()
    }
}