package hu.simplexion.z2.alarm.api

import hu.simplexion.z2.commons.i18n.LocalizedText
import hu.simplexion.z2.commons.util.UUID
import hu.simplexion.z2.service.runtime.Service

interface AlarmApi : Service {
    //suspend fun alarm(subject : UUID<*>, message : LocalizedText, ex : Exception)
}