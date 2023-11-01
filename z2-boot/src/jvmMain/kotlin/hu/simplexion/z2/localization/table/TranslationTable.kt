package hu.simplexion.z2.localization.table

import hu.simplexion.z2.commons.util.Lock
import hu.simplexion.z2.commons.util.UUID
import hu.simplexion.z2.commons.util.use
import hu.simplexion.z2.exposed.jvm
import hu.simplexion.z2.exposed.z2
import hu.simplexion.z2.localization.model.Locale
import hu.simplexion.z2.localization.model.Translation
import hu.simplexion.z2.localization.table.LocaleTable.Companion.localeTable
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import java.util.concurrent.ConcurrentHashMap

open class TranslationTable : Table(
    "z2_translation"
) {

    companion object {
        val translationTable = TranslationTable()
        val lock = Lock()
        val cache = ConcurrentHashMap<UUID<Locale>, List<Translation>>()
    }

    val locale = reference("owner", localeTable).index()
    val key = text("key").index()
    val value = text("value")
    val verified = bool("verified").default(false)

    fun list(inLocale: UUID<Locale>): List<Translation> {
        return lock.use {
            val cached = cache[inLocale]
            if (cached != null) return cached

            select { locale eq inLocale.jvm }
                .map { row ->
                    Translation().also {
                        it.locale = row[locale].z2()
                        it.key = row[key]
                        it.value = row[value]
                        it.verified = row[verified]
                    }
                }.also {
                    cache[inLocale] = it
                }
        }
    }

    fun put(translation: Translation) {
        insert {
            it[locale] = translation.locale.jvm
            it[key] = translation.key
            it[value] = translation.value
            it[verified] = translation.verified
            cache.clear()
        }
    }

}