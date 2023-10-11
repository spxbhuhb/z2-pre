package hu.simplexion.z2.auth.ui.account

import hu.simplexion.z2.auth.accountService
import hu.simplexion.z2.auth.model.AccountPrivate
import hu.simplexion.z2.baseStrings
import hu.simplexion.z2.browser.browserStrings
import hu.simplexion.z2.browser.css.gridGap24
import hu.simplexion.z2.browser.css.p24
import hu.simplexion.z2.browser.css.w400
import hu.simplexion.z2.browser.html.grid
import hu.simplexion.z2.browser.immaterial.schematic.field
import hu.simplexion.z2.browser.material.button.textButton
import hu.simplexion.z2.browser.material.modal.modal
import hu.simplexion.z2.commons.localization.text.LocalizedText
import hu.simplexion.z2.commons.util.fourRandomInt
import hu.simplexion.z2.commons.util.localLaunch

internal fun add() =
    accountModal(AccountPrivate(), baseStrings.addAccount, browserStrings.add) {
        val key =  (fourRandomInt() + fourRandomInt()).joinToString("-")
        accountService.add(it, false, key, emptyList())
    }

internal fun edit(role: AccountPrivate) =
    accountModal(role, baseStrings.editRole, browserStrings.add) {  }

internal fun accountModal(
    account: AccountPrivate,
    modalTitle: LocalizedText,
    buttonLabel: LocalizedText,
    onOk: suspend (role: AccountPrivate) -> Unit
) {
    localLaunch {
        modal(w400) {
            title(modalTitle)

            grid(p24, gridGap24) {
                field { account.email }
                field { account.fullName }
                field { account.phone }
            }

            buttons {
                textButton(browserStrings.cancel) { closeWith(false) }
                textButton(buttonLabel) {
                    localLaunch {
                        account.accountName = account.email ?: account.uuid.toShort()
                        onOk(account)
                        closeWith(true)
                    }
                }
            }
        }.show()
    }
}