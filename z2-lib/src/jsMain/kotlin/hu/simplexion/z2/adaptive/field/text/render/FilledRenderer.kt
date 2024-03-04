package hu.simplexion.z2.adaptive.field.text.render

import hu.simplexion.z2.browser.css.*
import hu.simplexion.z2.browser.material.px

class FilledRenderer : AbstractTextRenderer() {

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
}