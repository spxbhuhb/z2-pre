package hu.simplexion.z2.localization

import hu.simplexion.z2.exposed.implementations
import hu.simplexion.z2.exposed.tables
import hu.simplexion.z2.localization.impl.LocaleImpl.Companion.localeImpl
import hu.simplexion.z2.localization.table.LocaleTable.Companion.localeTable

fun localizationJvm() {
    localizationCommon()
    tables(localeTable)
    implementations(localeImpl)
}