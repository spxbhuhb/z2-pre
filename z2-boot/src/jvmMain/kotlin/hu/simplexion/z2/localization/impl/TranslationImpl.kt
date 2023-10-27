package hu.simplexion.z2.localization.impl

import hu.simplexion.z2.auth.context.ensure
import hu.simplexion.z2.auth.context.ensuredByLogic
import hu.simplexion.z2.auth.securityOfficerRole
import hu.simplexion.z2.commons.util.UUID
import hu.simplexion.z2.localization.api.TranslationApi
import hu.simplexion.z2.localization.model.Locale
import hu.simplexion.z2.localization.model.Translation
import hu.simplexion.z2.localization.table.TranslationTable.Companion.translationTable
import hu.simplexion.z2.service.ServiceImpl

class TranslationImpl : TranslationApi, ServiceImpl<TranslationImpl> {

    companion object {
        val translationImpl = TranslationImpl().internal
    }

    override suspend fun list(locale: UUID<Locale>): List<Translation> {
        ensuredByLogic("list of known translations is available for all users") // TODO think about translation availability
        return translationTable.list(locale)
    }

    override suspend fun put(translation: Translation) {
        ensure(securityOfficerRole)
        translationTable.put(translation)
    }

    override suspend fun remove(key: String, locale: UUID<Locale>?) {
        TODO("Not yet implemented")
    }

}