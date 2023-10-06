package hu.simplexion.z2.i18n.impl

import hu.simplexion.z2.auth.context.ensure
import hu.simplexion.z2.auth.context.ensuredByLogic
import hu.simplexion.z2.auth.context.has
import hu.simplexion.z2.auth.securityOfficerRole
import hu.simplexion.z2.commons.util.UUID
import hu.simplexion.z2.i18n.table.LanguageTable.Companion.languageTable
import hu.simplexion.z2.localization.api.LanguageApi
import hu.simplexion.z2.localization.model.Language
import hu.simplexion.z2.service.runtime.ServiceImpl

class LanguageImpl : LanguageApi, ServiceImpl<LanguageImpl> {

    companion object {
        val languageImpl = LanguageImpl()
    }

    override suspend fun list(): List<Language> {
        ensuredByLogic("list of known languages is available for all users")
        val all = serviceContext.has(securityOfficerRole).isAllowed
        return languageTable.list().filter { it.visible || all }
    }

    override suspend fun add(language: Language): UUID<Language> {
        ensure(securityOfficerRole)
        return languageTable.insert(language)
    }

    override suspend fun update(language: Language) {
        ensure(securityOfficerRole)
        return languageTable.update(language.id, language)
    }

    override suspend fun remove(uuid: UUID<Language>) {
        ensure(securityOfficerRole)
        languageTable.remove(uuid)
    }

    override suspend fun show(uuid: UUID<Language>) {
        ensure(securityOfficerRole)
        languageTable.setVisible(uuid, true)
    }

    override suspend fun hide(uuid: UUID<Language>) {
        ensure(securityOfficerRole)
        languageTable.setVisible(uuid, false)
    }

}