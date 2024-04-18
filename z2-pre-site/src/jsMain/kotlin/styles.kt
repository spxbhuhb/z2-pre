/*
 * Copyright © 2020-2023, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

import hu.simplexion.z2.browser.css.tableBorder
import hu.simplexion.z2.browser.css.tableBorderRadius
import hu.simplexion.z2.browser.css.tableHeaderBackground
import hu.simplexion.z2.browser.css.tableHeaderBottomBorder
import kotlinx.browser.document
import org.w3c.dom.HTMLElement

fun customizeStyles() {
    val root = document.querySelector(":root") as? HTMLElement ?: return
    with(root.style) {
        setProperty(tableBorder, "none")
        setProperty(tableBorderRadius, "0")
        setProperty(tableHeaderBackground, "var(--md-sys-color-surface-container-lowest)")
        setProperty(tableHeaderBottomBorder, "2px solid var(--md-sys-color-outline)")
    }
}