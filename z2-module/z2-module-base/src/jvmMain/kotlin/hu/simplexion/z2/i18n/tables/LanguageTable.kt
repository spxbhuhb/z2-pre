package hu.simplexion.z2.i18n.tables

import hu.simplexion.z2.commons.util.UUID
import hu.simplexion.z2.exposed.SchematicUuidTable
import hu.simplexion.z2.exposed.jvm
import hu.simplexion.z2.i18n.model.Language
import org.jetbrains.exposed.sql.update

open class LanguageTable : SchematicUuidTable<Language>(
    "i18n_language",
    Language()
) {

    companion object {
        val languageTable = LanguageTable()
    }

    val isoCode = char("isoCode", 2)
    val countryCode = char("countryCode", 2)
    val nativeName = varchar("nativeName", 30)
    val visible = bool("visible")

    fun setVisible(inLanguage : UUID<Language>, inVisible : Boolean) {
        update({ id eq inLanguage.jvm }) {
            it[visible] = inVisible
        }
    }
}