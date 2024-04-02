package hu.simplexion.z2.adaptive.modal

import hu.simplexion.z2.browser.material.modal.Modals

/**
 * Create and open a modal without a return value, built by [builder].
 */
fun modal(builder: ModalBuilder.() -> Unit) =
    ModalBuilder().apply(builder).apply { Modals += this.build() }
