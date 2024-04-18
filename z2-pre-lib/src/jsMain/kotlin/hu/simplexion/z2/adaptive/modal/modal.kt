package hu.simplexion.z2.adaptive.modal

import hu.simplexion.z2.browser.material.modal.Modals
import kotlinx.browser.document
import org.w3c.dom.HTMLElement

/**
 * Create and open a modal without a return value, built by [builder].
 */
fun modal(builder: ModalBuilder.() -> Unit) =
    ModalBuilder().apply(builder).apply {
        val element = document.activeElement
        if (element != null && element is HTMLElement) element.blur()
        Modals += this.build()
    }
