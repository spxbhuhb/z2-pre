package hu.simplexion.z2.i18n

import hu.simplexion.z2.exposed.implementations
import hu.simplexion.z2.exposed.tables
import hu.simplexion.z2.i18n.impl.LanguageImpl.Companion.languageImpl
import hu.simplexion.z2.i18n.tables.LanguageTable.Companion.languageTable

fun i18nJvm() {
    i18nCommon()
    tables(languageTable)
    implementations(languageImpl)
}