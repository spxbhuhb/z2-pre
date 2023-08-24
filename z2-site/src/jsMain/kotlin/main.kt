/*
 * Copyright © 2020-2023, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

import hu.simplexion.z2.browser.css.*
import hu.simplexion.z2.browser.demo.calendar.calendarDemo
import hu.simplexion.z2.browser.demo.form.formDemo
import hu.simplexion.z2.browser.demo.layout.containerDemo
import hu.simplexion.z2.browser.demo.material.*
import hu.simplexion.z2.browser.demo.pages.administration.administrationRouter
import hu.simplexion.z2.browser.demo.pages.loginDemo
import hu.simplexion.z2.browser.demo.pages.loginStrings
import hu.simplexion.z2.browser.demo.routing.routingRouter
import hu.simplexion.z2.browser.demo.strings
import hu.simplexion.z2.browser.demo.table.tableDemo
import hu.simplexion.z2.browser.html.*
import hu.simplexion.z2.browser.layout.Content
import hu.simplexion.z2.browser.material.basicIcons
import hu.simplexion.z2.browser.material.basicStrings
import hu.simplexion.z2.browser.material.button.iconButton
import hu.simplexion.z2.browser.material.button.outlinedIconButton
import hu.simplexion.z2.browser.material.navigation.navigationDrawer
import hu.simplexion.z2.browser.material.px
import hu.simplexion.z2.browser.material.searchbar.searchBar
import hu.simplexion.z2.browser.routing.BrowserRouter
import hu.simplexion.z2.browser.routing.NavRouter
import hu.simplexion.z2.browser.routing.Router
import hu.simplexion.z2.browser.routing.traceRouting

fun main() {
    traceRouting = true
    customizeStyles()
    Content.defaultLayout = { router, nav, content -> defaultLayout(router, nav, content) }
    mainRouter.receiver = Content
    mainRouter.start()
}

@Suppress("unused")
object mainRouter : BrowserRouter() {
    override val label = strings.main

    // @formatter:off
    val components       by componentRouter
    val pages            by pagesRouter
    val other            by otherRouter
    // @formatter:on

    override fun default(receiver: Z2, path: List<String>) {
        receiver.defaultLayout(this, { navigationDrawer(targets) }) { div { } }
    }

    override fun notFound(receiver: Z2, path: List<String>) {
        default(receiver, path)
    }
}

@Suppress("unused")
object componentRouter : NavRouter() {
    override val label = strings.components

    // @formatter:off
    val button           by render(strings.button)           { buttonDemo() }
    val calendar         by render(strings.calendar)         { calendarDemo() }
    val card             by render(strings.card)             { cardDemo() }
    val container        by render(strings.container)        { containerDemo() }
    val datepicker       by render(strings.datepicker)       { datepickerDemo() }
    val form             by render(strings.form)             { formDemo() }
    val menu             by render(strings.menu)             { menuDemo() }
    val modal            by render(strings.modal)            { modalDemo() }
    val navigationDrawer by render(strings.navigationDrawer) { navigationDrawerDemo() }
    val popup            by render(strings.popup)            { popupDemo() }
    val snackbar         by render(strings.snackbar)         { snackbarDemo() }
    val switch           by render(strings.switch)           { switchDemo() }
    val textField        by render(strings.textField)        { textFieldDemo() }
    val table            by render(strings.table)            { tableDemo() }
    // @formatter:on
}

@Suppress("unused")
object pagesRouter : NavRouter() {
    override val label = strings.pages

    // @formatter:off
    val login          by render(loginStrings.login)          { loginDemo() }
    val administration by administrationRouter
    // @formatter:on
}

@Suppress("unused")
object otherRouter : NavRouter() {
    override val label = strings.other

    // @formatter:off
    val router          by routingRouter
    // @formatter:on
}


fun Z2.defaultLayout(router: Router<Z2>, nav: Z2Builder, content: Z2Builder) {
    grid(wFull, hFull, pr16, pb16, boxSizingBorder) {
        gridTemplateRows = "min-content 1fr"
        gridTemplateColumns = "min-content 1fr"

        div(displayFlex, alignItemsCenter, h60, pl24, titleLarge) {
            text { strings.applicationTitle }
        }

        header()

        grid {
            gridTemplateRows = "min-content 1fr"
            gridTemplateColumns = "1fr"

            div(displayFlex, alignItemsCenter, pl24) {
                if (router != mainRouter) {
                    addClass(pt8)
                    outlinedIconButton(basicIcons.back, basicStrings.back) { router.up() }
                    div(pl8) { text { router.parent?.label } }
                }
            }

            nav()
        }

        content()
    }
}

fun Z2.header() =
    grid {
        gridTemplateColumns = "1fr min-content min-content"
        gridTemplateRows = "60px"
        gridGap = 8.px

        div(alignSelfCenter) {
            searchBar()
        }

        div(alignSelfCenter) {
            iconButton(basicIcons.settings, basicStrings.settings, weight = 300) { }
        }

        div(displayFlex, alignSelfCenter, borderOutline, borderRadius8, bodySmall, p4, pr8) {
            div(pl8, whiteSpaceNoWrap, pr8, alignSelfCenter) { text { "Tóth István Zoltán" } }
            div {
                style.height = 32.px
                style.width = 32.px
                style.borderRadius = 16.px
                style.backgroundColor = "green"
            }
        }
    }


