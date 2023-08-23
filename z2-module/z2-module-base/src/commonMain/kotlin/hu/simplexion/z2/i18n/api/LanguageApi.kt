package hu.simplexion.z2.i18n.api

import hu.simplexion.z2.commons.util.UUID
import hu.simplexion.z2.i18n.model.Language
import hu.simplexion.z2.service.runtime.Service

interface LanguageApi : Service {

    suspend fun list(): List<Language>

    suspend fun add(language: Language) : UUID<Language>

    suspend fun update(language: Language)

    suspend fun remove(uuid: UUID<Language>)

    suspend fun show(uuid : UUID<Language>)

    suspend fun hide(uuid : UUID<Language>)

}