package hu.simplexion.z2.localization.table

import hu.simplexion.z2.util.UUID
import hu.simplexion.z2.exposed.SchematicUuidTable
import hu.simplexion.z2.exposed.jvm
import hu.simplexion.z2.localization.model.Locale
import org.jetbrains.exposed.sql.update

open class LocaleTable : SchematicUuidTable<Locale>(
    "z2_locale",
    Locale()
) {

    companion object {
        val localeTable = LocaleTable()
    }

    val isoCode = char("isoCode", 2)
    val countryCode = char("countryCode", 2)
    val nativeName = varchar("nativeName", 30)
    val priority = integer("priority")
    val visible = bool("visible")

    fun setVisible(inLocale : UUID<Locale>, inVisible : Boolean) {
        update({ id eq inLocale.jvm }) {
            it[visible] = inVisible
        }
    }
}