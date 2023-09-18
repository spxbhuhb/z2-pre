package hu.simplexion.z2.browser.css

import hu.simplexion.z2.browser.html.Z2

infix fun Z2.css(className : String) : Z2 {
    addClass(className)
    return this
}