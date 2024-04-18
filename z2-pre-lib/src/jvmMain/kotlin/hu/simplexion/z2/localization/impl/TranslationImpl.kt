package hu.simplexion.z2.localization.impl

import hu.simplexion.z2.application.securityOfficerRole
import hu.simplexion.z2.auth.context.ensureOneOf
import hu.simplexion.z2.auth.context.ensuredByLogic
import hu.simplexion.z2.localization.api.TranslationApi
import hu.simplexion.z2.localization.model.Locale
import hu.simplexion.z2.localization.model.Translation
import hu.simplexion.z2.localization.table.LocaleTable.Companion.localeTable
import hu.simplexion.z2.localization.table.TranslationTable.Companion.translationTable
import hu.simplexion.z2.services.ServiceImpl
import hu.simplexion.z2.util.UUID

class TranslationImpl : TranslationApi, ServiceImpl<TranslationImpl> {

    companion object {
        val translationImpl = TranslationImpl().internal

        var translationEditRoles = arrayOf(securityOfficerRole.uuid)
    }

    override suspend fun list(locale: UUID<Locale>): List<Translation> {
        ensuredByLogic("list of known translations is available for all users") // TODO think about translation availability
        return translationTable.list(locale)
    }

    override suspend fun put(translation: Translation) {
        ensureOneOf(*translationEditRoles)
        translationTable.put(translation)
    }

    override suspend fun remove(key: String, locale: UUID<Locale>?) {
        TODO("Not yet implemented")
    }

    // -----------------------------------------------------------------------------------------------
    // Non-API functions
    // -----------------------------------------------------------------------------------------------

    fun put(uuid : UUID<*>, value : String) {
        put(uuid.toString(), value)
    }

    fun put(key : String, value : String) {
        for (locale in localeTable.query()) {
            translationTable.put(Translation().also {
                it.locale = locale.uuid
                it.key = key
                it.value = value
            })
        }
    }

}