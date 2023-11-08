package hu.simplexion.z2.localization.impl

import hu.simplexion.z2.auth.context.ensure
import hu.simplexion.z2.auth.context.ensuredByLogic
import hu.simplexion.z2.auth.context.has
import hu.simplexion.z2.auth.context.publicAccess
import hu.simplexion.z2.auth.securityOfficerRole
import hu.simplexion.z2.commons.util.UUID
import hu.simplexion.z2.localization.api.LocaleApi
import hu.simplexion.z2.localization.model.Locale
import hu.simplexion.z2.localization.table.LocaleTable.Companion.localeTable
import hu.simplexion.z2.localization.table.TranslationTable.Companion.translationTable
import hu.simplexion.z2.service.ServiceImpl

class LocaleImpl : LocaleApi, ServiceImpl<LocaleImpl> {

    companion object {
        val localeImpl = LocaleImpl().internal
    }

    override suspend fun list(): List<Locale> {
        ensuredByLogic("list of known locales is available for all users")
        val all = serviceContext.has(securityOfficerRole).isAllowed
        return localeTable.list().filter { it.visible || all }
    }

    override suspend fun add(locale: Locale): UUID<Locale> {
        ensure(securityOfficerRole)
        return localeTable.insert(locale)
    }

    override suspend fun update(locale: Locale) {
        ensure(securityOfficerRole)
        return localeTable.update(locale.uuid, locale)
    }

    override suspend fun remove(uuid: UUID<Locale>) {
        ensure(securityOfficerRole)
        localeTable.remove(uuid)
    }

    override suspend fun show(uuid: UUID<Locale>) {
        ensure(securityOfficerRole)
        localeTable.setVisible(uuid, true)
    }

    override suspend fun hide(uuid: UUID<Locale>) {
        ensure(securityOfficerRole)
        localeTable.setVisible(uuid, false)
    }

    override suspend fun load(uuid: UUID<Locale>, table: ByteArray) {
        ensure(securityOfficerRole)
        translationTable.load(uuid, table)
    }

    /**
     * Get the locale to use. Selection in order of preference:
     *
     * - exact match between [preferred] and the locale ISO code and country code
     * - match between ISO code from [preferred] and locale ISO code with the highest priority
     * - highest priority locale with no match
     *
     * @param preferred The preferred locale in form of `ii-CC` where `ii` is the ISO code,
     *                  `CC` is the country code, for example: "hu-HU". The country code is
     *                  optional.
     */
    override suspend fun getLocale(preferred: String): Locale {
        publicAccess()

        val parts = preferred.split("-")
        val isoCode = parts[0]
        val countryCode = if (parts.size < 2) "" else parts[1]

        var candidate = Locale().also { it.priority = Int.MIN_VALUE }
        var default = Locale().also { it.priority = Int.MIN_VALUE }

        for (locale in localeTable.list()) {
            if (locale.isoCode == isoCode) {
                if (locale.countryCode == countryCode) return locale
                if (candidate.priority < locale.priority) candidate = locale
            } else {
                if (default.priority < locale.priority) default = locale
            }
        }

        return when {
            candidate.priority != Int.MIN_VALUE -> candidate
            default.priority != Int.MIN_VALUE -> default
            else -> throw IllegalStateException("there are no locales")
        }
    }

}