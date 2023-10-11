package hu.simplexion.z2.browser.demo.pages.administration.locale

import hu.simplexion.z2.browser.browserStrings
import hu.simplexion.z2.browser.css.backgroundTransparent
import hu.simplexion.z2.browser.css.borderOutline
import hu.simplexion.z2.browser.css.p0
import hu.simplexion.z2.browser.demo.strings
import hu.simplexion.z2.browser.html.Z2
import hu.simplexion.z2.browser.immaterial.table.schematicColumn
import hu.simplexion.z2.browser.immaterial.table.table
import hu.simplexion.z2.browser.layout.surfaceContainer
import hu.simplexion.z2.browser.layout.surfaceContainerLowest
import hu.simplexion.z2.localization.localeService
import hu.simplexion.z2.localization.model.Locale

internal fun Z2.list() =
    surfaceContainerLowest(borderOutline) {
        surfaceContainer(p0, backgroundTransparent, scroll = false) {

            table<Locale> {

                title {
                    text = strings.languages
                    action(strings.addLocale) { add() }
                }

                rowId = { it.uuid }
                query = { localeService.list() }

                schematicColumn { Locale().isoCode }
                schematicColumn { Locale().countryCode }
                schematicColumn { Locale().nativeName }

                actionColumn {
                    action {
                        label = browserStrings.edit
                        handler = { edit(it) }
                    }
                }
            }
        }
    }
