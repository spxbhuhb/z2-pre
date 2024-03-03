package hu.simplexion.z2.adaptive.field.text.render

import hu.simplexion.z2.browser.css.*
import hu.simplexion.z2.browser.html.Z2
import hu.simplexion.z2.browser.html.div
import hu.simplexion.z2.browser.material.px

class BorderBottomRenderer : AbstractTextRenderer() {

    override fun Z2.leading() = div(alignSelfCenter, pt16)

    override fun Z2.trailing() = div(pr12, alignSelfCenter, pt16)

    override fun mainContainerStyles() {
        mainContainer.addCss(
            pt2,
            pr2,
            pb2,
            borderBottomWidth1, borderBottomSolid
        )
        mainContainer.htmlElement.style.paddingLeft = 1.px
        mainContainer.htmlElement.style.paddingTop = 1.px
        mainContainer.htmlElement.style.paddingRight = 3.px
    }

    override fun inputStyles() {
        super.inputStyles()
        input.addCss(pt20)
    }

    override fun animationStyles() {
        with(animation) {
            with(htmlElement.style) {
                borderStyle = "solid"
                top = 46.px
                height = 10.px
                borderWidth = 0.px
                borderBottomWidth = 2.px
            }
        }
    }

    override fun hideLabel() {
        label.addCss(displayNone)
    }

    override fun showLabel() {
        label.removeCss(displayNone)
    }

}
