package hu.simplexion.z2.localization

import hu.simplexion.z2.exposed.implementations
import hu.simplexion.z2.exposed.tables
import hu.simplexion.z2.localization.impl.LocaleImpl.Companion.localeImpl
import hu.simplexion.z2.localization.impl.TranslationImpl.Companion.translationImpl
import hu.simplexion.z2.localization.table.LocaleTable.Companion.localeTable
import hu.simplexion.z2.localization.table.TranslationTable.Companion.translationTable

fun localizationJvm() {
    localizationCommon()
    tables(localeTable, translationTable)
    implementations(localeImpl, translationImpl)
}