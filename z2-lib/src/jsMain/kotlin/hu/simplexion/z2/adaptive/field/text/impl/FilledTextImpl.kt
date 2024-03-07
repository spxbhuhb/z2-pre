package hu.simplexion.z2.adaptive.field.text.impl

import hu.simplexion.z2.adaptive.impl.AdaptiveImpl
import hu.simplexion.z2.adaptive.impl.AdaptiveImplFactory
import hu.simplexion.z2.browser.css.*
import hu.simplexion.z2.browser.html.Z2
import hu.simplexion.z2.browser.material.px
import hu.simplexion.z2.util.UUID

class FilledTextImpl(parent: Z2) : AbstractTextImpl(parent) {

    override fun mainContainerStyles() {
        mainContainer.addCss(
            borderBottomWidth1, borderBottomSolid,
            borderColorOutline,
            surfaceContainerHighest,
            borderTopLeftRadiusExtraSmall,
            borderTopRightRadiusExtraSmall,
           // pt2,
           // pr2,
           pb2,
        )
        mainContainer.htmlElement.style.paddingLeft = 1.px
        mainContainer.htmlElement.style.paddingTop = 1.px
        mainContainer.htmlElement.style.paddingRight = 3.px

    }

    override fun animationStyles() {
        with(animation.htmlElement.style) {
            borderStyle = "solid"
            top = 46.px
            height = 10.px
            borderWidth = 0.px
            borderBottomWidth = 2.px
        }
    }

    override fun patchLeading() {
        super.patchLeading()

        if (isEmpty && ! hasFocus) {
            input.removeCss(pt20)
        } else {
            input.addCss(pt20)
        }

        if (readOnly) {
            mainContainer.removeCss(surfaceContainerHighest)
            mainContainer.addCss(borderOutline, borderRadius4)
        } else {
            mainContainer.addCss(surfaceContainerHighest)
            mainContainer.removeCss(borderOutline, borderRadius4)
        }
    }

    companion object : AdaptiveImplFactory(UUID("21538bd7-ff59-4bb7-b30c-d225b60bae35")) {
        override fun new(parent: AdaptiveImpl) = FilledTextImpl(parent as Z2)
    }
}