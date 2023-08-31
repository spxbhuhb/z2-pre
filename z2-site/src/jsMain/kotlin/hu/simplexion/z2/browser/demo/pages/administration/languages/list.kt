package hu.simplexion.z2.browser.demo.pages.administration.languages

import hu.simplexion.z2.browser.css.backgroundTransparent
import hu.simplexion.z2.browser.css.borderOutline
import hu.simplexion.z2.browser.css.p0
import hu.simplexion.z2.browser.html.Z2
import hu.simplexion.z2.browser.layout.container
import hu.simplexion.z2.browser.layout.lowest
import hu.simplexion.z2.browser.material.basicStrings
import hu.simplexion.z2.browser.components.table.schematicColumn
import hu.simplexion.z2.browser.components.table.table

internal fun Z2.list() =
    lowest(borderOutline) {

        val data = listOf(
            Language().apply {
                isoCode = "hu"
                countryCode = "HU"
                nativeName = "Magyar"
            }
        )

        container(p0, backgroundTransparent, scroll = false) {

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
                        label = basicStrings.edit
                        handler = { edit(it) }
                    }
                }
            }
        }
    }
