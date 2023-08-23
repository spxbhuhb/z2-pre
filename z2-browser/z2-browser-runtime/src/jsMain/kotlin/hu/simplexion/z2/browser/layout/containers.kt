package hu.simplexion.z2.browser.layout

import hu.simplexion.z2.browser.css.hFull
import hu.simplexion.z2.browser.css.overflowYAuto
import hu.simplexion.z2.browser.html.Z2
import hu.simplexion.z2.browser.html.div

fun Z2.lowest(vararg classes : String, scroll : Boolean = true, fullHeight: Boolean = true, builder : Z2.() -> Unit) =
    container(classes, "surface-container-lowest", scroll, fullHeight, builder)

fun Z2.low(vararg classes : String, scroll : Boolean = true, fullHeight: Boolean = true, builder : Z2.() -> Unit) =
    container(classes,"surface-container-low", scroll, fullHeight, builder)

fun Z2.container(vararg classes : String, scroll : Boolean = true, fullHeight: Boolean = true, builder : Z2.() -> Unit) =
    container(classes,"surface-container", scroll, fullHeight, builder)

fun Z2.high(vararg classes : String, scroll : Boolean = true, fullHeight: Boolean = true, builder : Z2.() -> Unit) =
    container(classes,"surface-container-high", scroll, fullHeight, builder)

fun Z2.highest(vararg classes : String, scroll : Boolean = true, fullHeight: Boolean = true, builder : Z2.() -> Unit) =
    container(classes,"surface-container-highest", scroll, fullHeight, builder)

internal fun Z2.container(classes : Array<out String>, type : String, scroll : Boolean, fullHeight: Boolean, builder : Z2.() -> Unit) =
    div(type) {
        if (classes.isNotEmpty()) addClass(*classes)
        if (scroll) addClass(overflowYAuto)
        if (fullHeight) addClass(hFull)
        builder()
    }

