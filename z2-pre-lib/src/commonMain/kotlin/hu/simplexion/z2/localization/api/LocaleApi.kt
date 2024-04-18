package hu.simplexion.z2.localization.api

import hu.simplexion.z2.util.UUID
import hu.simplexion.z2.localization.model.Locale
import hu.simplexion.z2.services.Service

interface LocaleApi : Service {

    suspend fun list(): List<Locale>

    suspend fun add(locale: Locale) : UUID<Locale>

    suspend fun update(locale: Locale)

    suspend fun remove(uuid: UUID<Locale>)

    suspend fun show(uuid : UUID<Locale>)

    suspend fun hide(uuid : UUID<Locale>)

    suspend fun load(uuid : UUID<Locale>, table : ByteArray)

    suspend fun getLocale(preferred : String) : Locale
    
}