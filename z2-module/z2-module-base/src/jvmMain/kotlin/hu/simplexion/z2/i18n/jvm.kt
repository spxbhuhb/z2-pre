package hu.simplexion.z2.i18n

import hu.simplexion.z2.exposed.implementations
import hu.simplexion.z2.exposed.tables
import hu.simplexion.z2.i18n.impl.LanguageImpl.Companion.languageImpl
import hu.simplexion.z2.i18n.table.LanguageTable.Companion.languageTable
import hu.simplexion.z2.localization.localizationCommon

fun i18nJvm() {
    localizationCommon()
    tables(languageTable)
    implementations(languageImpl)
}