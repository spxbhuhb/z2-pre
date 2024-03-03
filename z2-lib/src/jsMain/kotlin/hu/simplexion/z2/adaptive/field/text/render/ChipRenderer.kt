package hu.simplexion.z2.adaptive.field.text.render

import hu.simplexion.z2.browser.css.*
import hu.simplexion.z2.browser.html.Z2
import hu.simplexion.z2.browser.html.div
import hu.simplexion.z2.browser.material.px

class ChipRenderer : AbstractTextRenderer() {

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

}