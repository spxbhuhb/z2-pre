package hu.simplexion.z2.i18n.ui

import hu.simplexion.z2.browser.browserStrings
import hu.simplexion.z2.browser.css.backgroundTransparent
import hu.simplexion.z2.browser.css.borderOutline
import hu.simplexion.z2.browser.css.p0
import hu.simplexion.z2.browser.html.Z2
import hu.simplexion.z2.browser.immaterial.table.schematicColumn
import hu.simplexion.z2.browser.immaterial.table.table
import hu.simplexion.z2.browser.layout.surfaceContainer
import hu.simplexion.z2.browser.layout.surfaceContainerLowest
import hu.simplexion.z2.i18n.languages
import hu.simplexion.z2.localization.model.Language
import hu.simplexion.z2.localization.ui.localizationStrings

internal fun Z2.list() =
    surfaceContainerLowest(borderOutline) {
        surfaceContainer(p0, backgroundTransparent, scroll = false) {

            table<Language> {

                title {
                    text = localizationStrings.languages
                    action(localizationStrings.addLanguage) { add() }
                }

                rowId = { it.id }
                query = { languages.list().map {
                   // println(it.dump())
                    it
                } }

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
