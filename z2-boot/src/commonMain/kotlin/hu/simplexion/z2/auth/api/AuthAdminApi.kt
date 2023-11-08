package hu.simplexion.z2.auth.api

import hu.simplexion.z2.auth.model.SecurityPolicy
import hu.simplexion.z2.service.Service

interface AuthAdminApi : Service {

    suspend fun getPolicy() : SecurityPolicy

    suspend fun setPolicy(policy : SecurityPolicy)

}