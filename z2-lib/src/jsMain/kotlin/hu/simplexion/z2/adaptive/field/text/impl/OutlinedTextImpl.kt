package hu.simplexion.z2.adaptive.field.text.impl

import hu.simplexion.z2.adaptive.impl.AdaptiveImpl
import hu.simplexion.z2.adaptive.impl.AdaptiveImplFactory
import hu.simplexion.z2.browser.css.*
import hu.simplexion.z2.browser.html.Z2
import hu.simplexion.z2.browser.material.px
import hu.simplexion.z2.util.UUID

class OutlinedTextImpl(parent: Z2) : AbstractTextImpl(parent) {

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

    companion object : AdaptiveImplFactory(UUID("691743a1-adee-4a47-8acd-bb29665acb7a")) {
        override fun new(parent: AdaptiveImpl) = OutlinedTextImpl(parent as Z2)
    }
}