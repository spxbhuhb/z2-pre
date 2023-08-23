package hu.simplexion.z2.i18n

import hu.simplexion.z2.exposed.registerWithTransaction
import hu.simplexion.z2.i18n.impl.LanguageImpl
import hu.simplexion.z2.i18n.tables.LanguageTable
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

fun i18n() {

    i18nCommon()

    transaction {
        SchemaUtils.createMissingTablesAndColumns(
            LanguageTable()
        )
    }

    registerWithTransaction(
        LanguageImpl()
    )

}