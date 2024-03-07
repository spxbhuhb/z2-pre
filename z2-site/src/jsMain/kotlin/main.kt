/*
 * Copyright © 2020-2023, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

import hu.simplexion.z2.adaptive.field.registerFieldImpl
import hu.simplexion.z2.adaptive.field.select.demo.entitySelectDemo
import hu.simplexion.z2.adaptive.field.select.demo.selectFieldPlayground
import hu.simplexion.z2.adaptive.field.text.demo.textFieldPlayground
import hu.simplexion.z2.baseStrings
import hu.simplexion.z2.browser.browserIcons
import hu.simplexion.z2.browser.browserStrings
import hu.simplexion.z2.browser.css.*
import hu.simplexion.z2.browser.demo.calendar.calendarDemo
import hu.simplexion.z2.browser.demo.chart.chartDemo
import hu.simplexion.z2.browser.demo.components.select.selectDemo
import hu.simplexion.z2.browser.demo.field.stereotype.decimalDemo
import hu.simplexion.z2.browser.demo.form.formDemo
import hu.simplexion.z2.browser.demo.immaterial.dateTimePickerDemo
import hu.simplexion.z2.browser.demo.layout.containerDemo
import hu.simplexion.z2.browser.demo.material.*
import hu.simplexion.z2.browser.demo.pages.loginDemo
import hu.simplexion.z2.browser.demo.routing.routingRouter
import hu.simplexion.z2.browser.demo.search.searchDemo
import hu.simplexion.z2.browser.demo.strings
import hu.simplexion.z2.browser.demo.table.tableDemo
import hu.simplexion.z2.browser.html.*
import hu.simplexion.z2.browser.layout.Content
import hu.simplexion.z2.browser.material.button.iconButton
import hu.simplexion.z2.browser.material.button.outlinedIconButton
import hu.simplexion.z2.browser.material.navigation.navigationDrawer
import hu.simplexion.z2.browser.material.px
import hu.simplexion.z2.browser.material.searchbar.searchBar
import hu.simplexion.z2.browser.routing.BrowserRouter
import hu.simplexion.z2.browser.routing.NavRouter
import hu.simplexion.z2.browser.routing.Router
import hu.simplexion.z2.localization.text.commonStrings
import hu.simplexion.z2.localization.text.dateTimeStrings
import hu.simplexion.z2.schematic.schema.validation.validationStrings
import hu.simplexion.z2.site.boot.bootJs
import hu.simplexion.z2.util.localLaunch

fun main() {
    localLaunch {
        bootJs()

        registerFieldImpl()

        commonStrings = strings
        dateTimeStrings = strings
        browserStrings = strings
        baseStrings = strings
        validationStrings = strings

        customizeStyles()
        Content.defaultLayout = { router, nav, content -> defaultLayout(router, nav, content) }
        mainRouter.receiver = Content
        mainRouter.start()
    }
}

@Suppress("unused")
object mainRouter : BrowserRouter() {
    override val label = strings.main

    // @formatter:off
    val components       by componentRouter
    val adaptive         by adaptiveRouter
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
object componentRouter : NavRouter(loggedIn = false) {
    override val label = strings.components

    // @formatter:off
    val button           by render(strings.button)           { buttonDemo() }
    val calendar         by render(strings.calendar)         { calendarDemo() }
    val card             by render(strings.card)             { cardDemo() }
    val chart            by render(strings.chart)            { chartDemo() }
    val container        by render(strings.container)        { containerDemo() }
    val datepicker       by render(strings.datepicker)       { datepickerDemo() }
    val dateTimePicker   by render(strings.datetimepicker)   { dateTimePickerDemo() }
    val decimal          by render(strings.decimal)          { decimalDemo() }
    val enum             by render(strings.enum)             { enumDemo() }
    val form             by render(strings.form)             { formDemo() }
    val menu             by render(strings.menu)             { menuDemo() }
    val modal            by render(strings.modal)            { modalDemo() }
    val navigationDrawer by render(strings.navigationDrawer) { navigationDrawerDemo() }
    val popup            by render(strings.popup)            { popupDemo() }
    val radioButton      by render(strings.radioButton)      { radioButtonDemo() }
    val search           by render(strings.search)           { searchDemo() }
    val select           by render(strings.select)           { selectDemo() }
    val snackbar         by render(strings.snackbar)         { snackbarDemo() }
    val switch           by render(strings.switch)           { switchDemo() }
    val table            by render(strings.table)            { tableDemo() }
    val timepicker       by render(strings.timepicker)       { timepickerDemo() }
    val textField        by render(strings.textField)        { textFieldDemo() }
    // @formatter:on
}


@Suppress("unused")
object adaptiveRouter : NavRouter(loggedIn = false) {
    override val label = strings.adaptive

    // @formatter:off
    val text           by render(strings.textField)         { textFieldPlayground() }
    val entitySelect   by render(strings.entitySelect)      { entitySelectDemo() }
    val select         by render(strings.select)            { selectFieldPlayground() }
    // @formatter:on
}

@Suppress("unused")
object pagesRouter : NavRouter() {
    override val label = strings.pages

    // @formatter:off
    val login          by render(strings.login)          { loginDemo() }
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
    grid(wFull, heightFull, pr16, pb16, boxSizingBorderBox) {
        gridTemplateRows = "min-content 1fr"
        gridTemplateColumns = "240px 1fr"

        div(displayFlex, alignItemsCenter, h60, pl24, titleLarge) {
            text { strings.applicationTitle }
        }

        header()

        grid(positionRelative, heightFull, overflowHidden) {
            gridTemplateRows = "min-content 1fr"
            gridTemplateColumns = "1fr"

            div(displayFlex, alignItemsCenter, pl24) {
                if (router != mainRouter) {
                    addCss(pt8)
                    outlinedIconButton(browserIcons.back, browserStrings.back) { router.up() }
                    div(pl8) { text { router.parent?.label } }
                }
            }

            div(heightFull, overflowYAuto) {
                nav()
            }
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
            iconButton(browserIcons.settings, browserStrings.settings, weight = 300) { }
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


