package hu.simplexion.z2.adaptive.field.text.render

import hu.simplexion.z2.browser.css.*
import hu.simplexion.z2.browser.material.px

class OutlinedRenderer : AbstractTextRenderer() {

    override fun mainContainerStyles() {
        mainContainer.addCss(
            pr2, pb2,
            borderWidth1, borderSolid, borderColorOutline,
            borderRadiusExtraSmall
        )
    }

    override fun animationStyles() {
        with(animation) {
            with(htmlElement.style) {
                borderStyle = "solid"
                height = baseHeight
                borderWidth = 2.px
            }
            addCss(borderRadiusExtraSmall, pointerEventsNone)
        }
    }
}