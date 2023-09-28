package hu.simplexion.z2.browser.demo.pages.administration.languages

import hu.simplexion.z2.browser.browserStrings
import hu.simplexion.z2.browser.css.backgroundTransparent
import hu.simplexion.z2.browser.css.borderOutline
import hu.simplexion.z2.browser.css.p0
import hu.simplexion.z2.browser.html.Z2
import hu.simplexion.z2.browser.immaterial.table.schematicColumn
import hu.simplexion.z2.browser.immaterial.table.table
import hu.simplexion.z2.browser.layout.surfaceContainer
import hu.simplexion.z2.browser.layout.surfaceContainerLowest
import hu.simplexion.z2.i18n.model.Language

internal fun Z2.list() =
    surfaceContainerLowest(borderOutline) {

        val data = listOf(
            Language().apply {
                isoCode = "hu"
                countryCode = "HU"
                nativeName = "Magyar"
            }
        )

        surfaceContainer(p0, backgroundTransparent, scroll = false) {

            table<Language> {

                title {
                    text = strings.languages
                    action(strings.addLanguage) { add() }
                }

                rowId = { it.id }
                query = { data }

                schematicColumn { Language().isoCode }
                schematicColumn { Language().countryCode }
                schematicColumn { Language().nativeName }

                actionColumn {
                    action {
                        label = browserStrings.edit
                        handler = { edit(it) }
                    }
                }
            }
        }
    }
