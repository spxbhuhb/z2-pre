package hu.simplexion.z2.browser.layout

import hu.simplexion.z2.browser.css.*
import hu.simplexion.z2.browser.html.Z2
import hu.simplexion.z2.browser.html.Z2Builder
import hu.simplexion.z2.browser.html.div
import hu.simplexion.z2.browser.html.gridTemplateRows
import hu.simplexion.z2.commons.localization.text.LocalizedText

fun Z2.surfaceContainerLowest(vararg classes: String, scroll: Boolean = true, fullHeight: Boolean = true, builder: Z2.() -> Unit) =
    container(classes, "surface-container-lowest", scroll, fullHeight, builder)

fun Z2.surfaceContainerLow(vararg classes: String, scroll: Boolean = true, fullHeight: Boolean = true, builder: Z2.() -> Unit) =
    container(classes, "surface-container-low", scroll, fullHeight, builder)

fun Z2.surfaceContainer(vararg classes: String, scroll: Boolean = true, fullHeight: Boolean = true, builder: Z2.() -> Unit) =
    container(classes, "surface-container", scroll, fullHeight, builder)

fun Z2.surfaceContainerHigh(vararg classes: String, scroll: Boolean = true, fullHeight: Boolean = true, builder: Z2.() -> Unit) =
    container(classes, "surface-container-high", scroll, fullHeight, builder)

fun Z2.surfaceContainerHighest(vararg classes: String, scroll: Boolean = true, fullHeight: Boolean = true, builder: Z2.() -> Unit) =
    container(classes, surfaceContainerHighest, scroll, fullHeight, builder)

fun Z2.container(classes: Array<out String>, type: String?, scroll: Boolean, fullHeight: Boolean, builder: Z2.() -> Unit) =
    div(boxSizingBorderBox, borderRadius16, p16) {
        if (type != null) addClass(type)
        if (classes.isNotEmpty()) addClass(*classes)
        if (scroll) addClass(overflowYAuto)
        if (fullHeight) addClass(heightFull)
        builder()
    }

// TODO clean up the container mess
fun Z2.container(vararg classes: String, scroll: Boolean = true, fullHeight: Boolean = true, builder: Z2.() -> Unit) =
    container(classes, null, scroll, fullHeight, builder)

fun Z2.scrolledBoxWithLabel(label: LocalizedText, builder: Z2Builder) =
    div(displayGrid, heightFull, boxSizingBorderBox, borderOutline, borderRadius4, positionRelative) {
        gridTemplateRows = "min-content 1fr"
        div(labelMedium, pl12, pt8, pb4) { + label }
        div(overflowYAuto, heightFull) {
            builder()
        }
    }