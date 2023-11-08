package hu.simplexion.z2.auth.impl

import hu.simplexion.z2.auth.api.AuthAdminApi
import hu.simplexion.z2.auth.context.ensureTechnicalAdmin
import hu.simplexion.z2.auth.model.SecurityPolicy
import hu.simplexion.z2.auth.securityOfficerUuid
import hu.simplexion.z2.schematic.ensureValid
import hu.simplexion.z2.service.ServiceImpl
import hu.simplexion.z2.setting.impl.SettingImpl.Companion.settingImpl

class AuthAdminImpl : AuthAdminApi, ServiceImpl<AuthAdminImpl> {

    companion object {
        val authAdminImpl = AuthAdminImpl().internal
        val KEY = "securityPolicy"
    }

    override suspend fun getPolicy(): SecurityPolicy {
        ensureTechnicalAdmin()
        return settingImpl(serviceContext).get(securityOfficerUuid, KEY, SecurityPolicy())
    }

    override suspend fun setPolicy(policy: SecurityPolicy) {
        ensureTechnicalAdmin()
        ensureValid(policy)

        settingImpl(serviceContext).put(securityOfficerUuid, KEY, policy)
    }


}