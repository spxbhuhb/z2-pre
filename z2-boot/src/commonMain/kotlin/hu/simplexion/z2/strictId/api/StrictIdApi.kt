package hu.simplexion.z2.strictId.api

import hu.simplexion.z2.service.runtime.Service

interface StrictIdApi : Service {

    /**
     * Get the next id for the given key. Adds the key if it doesn't already exist.
     * In that case the function returns with 1.
     */
    suspend fun next(key : String) : Long

}