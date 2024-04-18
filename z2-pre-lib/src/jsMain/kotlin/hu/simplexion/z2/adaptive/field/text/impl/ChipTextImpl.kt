package hu.simplexion.z2.adaptive.field.text.impl

import hu.simplexion.z2.adaptive.impl.AdaptiveImpl
import hu.simplexion.z2.adaptive.impl.AdaptiveImplFactory
import hu.simplexion.z2.browser.css.*
import hu.simplexion.z2.browser.html.Z2
import hu.simplexion.z2.browser.html.div
import hu.simplexion.z2.browser.material.px
import hu.simplexion.z2.util.UUID

class ChipTextImpl(parent: Z2) : AbstractTextImpl(parent) {

    override val baseHeight = 32.px

    override fun mainContainerStyles() {
        mainContainer.addCss(
            borderWidth1, borderSolid, borderColorOutline,
            borderRadius8,
            labelLarge
        )
    }

    override fun Z2.label(): Z2 = div(displayNone)

    override fun inputStyles() {
        input.addCss(labelLarge)
        inputElement.style.lineHeight = 24.px
    }

    override fun animationStyles() {
        with(animation) {
            with(htmlElement.style) {
                borderStyle = "solid"
                height = baseHeight
                borderWidth = 2.px
            }
            addCss(borderRadius8, pointerEventsNone)
        }
    }

    override fun showLabel() {
        // chip never shows the label
    }

    companion object : AdaptiveImplFactory(UUID("5c7306d1-53ef-4f96-aef8-486f638aa5b8")) {
        override fun new(parent: AdaptiveImpl) = ChipTextImpl(parent as Z2)
    }

}