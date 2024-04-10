package hu.simplexion.z2.adaptive.modal

import hu.simplexion.z2.browser.browserStrings
import hu.simplexion.z2.browser.css.*
import hu.simplexion.z2.browser.html.*
import hu.simplexion.z2.browser.material.button.filledLaunchButton
import hu.simplexion.z2.browser.material.button.textButton
import hu.simplexion.z2.browser.material.modal.Modals
import hu.simplexion.z2.browser.material.vh
import hu.simplexion.z2.browser.material.vw
import hu.simplexion.z2.localization.locales.localeCapitalized
import hu.simplexion.z2.localization.runtime.localized
import hu.simplexion.z2.localization.text.LocalizedText

class ModalBuilder {

    var title: LocalizedText? = null

    var supportingText: LocalizedText? = null

    var headerBuilder: Z2Builder = {
        if (title != null) {
            div(pb16, pt16, displayFlex, alignItemsCenter, headlineSmall, borderBottomOutlineVariant) {
                span(pl24, pr24) { + title?.localeCapitalized }
            }
        }
    }

    var contentBuilder: Z2Builder = {
        div(positionRelative) {
            style.overflowY = "scroll"
            div(pl24, pr24, heightMinContent) {
                supportingTextBuilder()
                bodyBuilder()
            }
            footerBuilder?.let { it() }
        }
    }

    var supportingTextBuilder: Z2Builder = {
        supportingText?.let {
            div(p24, pt0, bodyMedium) {
                text { it }
            }
        }
    }

    var bodyBuilder: Z2Builder = { }

    var footerBuilder: Z2Builder? = null

    fun title(title: String) {
        this.title = title.localized
    }

    fun title(title: LocalizedText) {
        this.title = title
    }

    fun header(builder: Z2Builder) {
        headerBuilder = builder
    }

    fun body(builder: Z2Builder) {
        bodyBuilder = builder
    }

    fun footer(builder: Z2Builder) {
        footerBuilder = builder
    }

    fun save(label: LocalizedText? = null, saveFun: suspend () -> Unit) {
        footerBuilder = {
            defaultFooter {
                textButton(browserStrings.cancel) { close() }
                filledLaunchButton(label ?: browserStrings.save) {
                    saveFun()
                }
            }
        }
    }

    fun ok() {
        footerBuilder = {
            defaultFooter {
                div { }
                filledLaunchButton(browserStrings.ok) {
                    close()
                }
            }
        }
    }

    fun Z2.defaultFooter(insideFun: Z2Builder) {
        div(borderTopOutlineVariant) {
            grid(
                pl16,
                pr16,
                pt12,
                pb12,
                gridAutoFlowColumn,
                gridAutoColumnsMinContent,
                gridGap16,
                justifyContentSpaceBetween
            ) {
                insideFun()
            }
        }
    }

    fun close() {
        Modals.close()
    }

    fun build() =
        Z2 {
            gridTemplateColumns = "1fr"
            gridTemplateRows = "min-content 1fr"

            style.maxHeight = 98.vh
            style.maxWidth = 98.vw

            addCss(
                boxSizingBorderBox,
                onSurfaceText,
                surface,
                borderRadius12,
                displayGrid,
                positionRelative,
                overflowHidden
            )

            headerBuilder()
            contentBuilder()
        }
}