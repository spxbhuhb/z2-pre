package hu.simplexion.z2.site.impl

import hu.simplexion.z2.auth.context.ensureLoggedIn
import hu.simplexion.z2.auth.context.ensureTest
import hu.simplexion.z2.auth.context.principal
import hu.simplexion.z2.auth.util.runAsSecurityOfficer
import hu.simplexion.z2.auth.util.runTransactionAsSecurityOfficer
import hu.simplexion.z2.service.ServiceImpl
import hu.simplexion.z2.setting.impl.SettingImpl.Companion.settingImpl
import hu.simplexion.z2.site.api.SiteApi
import hu.simplexion.z2.site.model.SiteSettings

class SiteImpl : SiteApi, ServiceImpl<SiteImpl> {

    companion object {
        val siteImpl = SiteImpl().internal

        const val SITE_SETTINGS_KEY = "site" // TODO think about hard-coded keys
    }

    override suspend fun siteUrl(): String {
        ensureLoggedIn()

        // TODO cache site settings?
        val settings = runAsSecurityOfficer {
            settingImpl(it).get(it.principal, SITE_SETTINGS_KEY, SiteSettings())
        }

        return settings.protocol + "://" + settings.host + ":" + settings.port + "/"
    }

    override suspend fun isTest(): Boolean {
        // TODO cache testing
        return runAsSecurityOfficer {
            settingImpl(it).get(it.principal, SITE_SETTINGS_KEY, SiteSettings()).test
        }
    }

    override suspend fun testPassword(): String {
        ensureLoggedIn()
        ensureTest()
        return runAsSecurityOfficer {
            settingImpl(it).get(it.principal, SITE_SETTINGS_KEY, SiteSettings()).testPassword
        }
    }

    fun testEmail(): String? {
        return runTransactionAsSecurityOfficer { context ->
            settingImpl(context).get(context.principal, SITE_SETTINGS_KEY, SiteSettings()).testEmailAddress.takeIf { it.isNotBlank() }
        }
    }
}