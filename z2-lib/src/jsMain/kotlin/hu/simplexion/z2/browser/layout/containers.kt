package hu.simplexion.z2.browser.layout

import hu.simplexion.z2.adaptive.browser.CssClass
import hu.simplexion.z2.browser.css.*
import hu.simplexion.z2.browser.html.Z2
import hu.simplexion.z2.browser.html.Z2Builder
import hu.simplexion.z2.browser.html.div
import hu.simplexion.z2.browser.html.gridTemplateRows
import hu.simplexion.z2.localization.text.LocalizedText

fun Z2.surfaceContainerLowest(vararg classes: CssClass, scroll: Boolean = true, fullHeight: Boolean = true, builder: Z2.() -> Unit) =
    container(classes, surfaceContainerLowest, scroll, fullHeight, builder)

fun Z2.surfaceContainerLow(vararg classes: CssClass, scroll: Boolean = true, fullHeight: Boolean = true, builder: Z2.() -> Unit) =
    container(classes, surfaceContainerLow, scroll, fullHeight, builder)

fun Z2.surfaceContainer(vararg classes: CssClass, scroll: Boolean = true, fullHeight: Boolean = true, builder: Z2.() -> Unit) =
    container(classes, surfaceContainer, scroll, fullHeight, builder)

fun Z2.surfaceContainerHigh(vararg classes: CssClass, scroll: Boolean = true, fullHeight: Boolean = true, builder: Z2.() -> Unit) =
    container(classes, surfaceContainerHigh, scroll, fullHeight, builder)

fun Z2.surfaceContainerHighest(vararg classes: CssClass, scroll: Boolean = true, fullHeight: Boolean = true, builder: Z2.() -> Unit) =
    container(classes, surfaceContainerHighest, scroll, fullHeight, builder)

fun Z2.container(classes: Array<out CssClass>, type: CssClass?, scroll: Boolean, fullHeight: Boolean, builder: Z2.() -> Unit) =
    div(boxSizingBorderBox, borderRadius16, p16) {
        if (type != null) addCss(type)
        if (classes.isNotEmpty()) addCss(*classes)
        if (scroll) addCss(overflowYAuto)
        if (fullHeight) addCss(heightFull)
        builder()
    }

// TODO clean up the container mess
fun Z2.container(vararg classes: CssClass, scroll: Boolean = true, fullHeight: Boolean = true, builder: Z2.() -> Unit) =
    container(classes, null, scroll, fullHeight, builder)

fun Z2.scrolledBoxWithLabel(label: LocalizedText, border : Boolean = true, builder: Z2Builder) =
    div(displayGrid, heightFull, boxSizingBorderBox, borderRadius4, positionRelative) {
        if (border) addCss(borderOutline)
        gridTemplateRows = "min-content 1fr"
        div(labelMedium, pl12, pt8, pb4) { + label }
        div(overflowYAuto, heightFull) {
            builder()
        }
    }