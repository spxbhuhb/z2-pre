package hu.simplexion.z2.localization.impl

import hu.simplexion.z2.auth.context.ensure
import hu.simplexion.z2.auth.context.ensuredByLogic
import hu.simplexion.z2.auth.context.has
import hu.simplexion.z2.auth.securityOfficerRole
import hu.simplexion.z2.commons.util.UUID
import hu.simplexion.z2.localization.api.LocaleApi
import hu.simplexion.z2.localization.model.Locale
import hu.simplexion.z2.localization.table.LocaleTable.Companion.localeTable
import hu.simplexion.z2.service.ServiceImpl

class LocaleImpl : LocaleApi, ServiceImpl<LocaleImpl> {

    companion object {
        val localeImpl = LocaleImpl()
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
        return localeTable.update(locale.id, locale)
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

}