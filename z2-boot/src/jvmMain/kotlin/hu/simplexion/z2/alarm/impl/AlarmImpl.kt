package hu.simplexion.z2.alarm.impl

import hu.simplexion.z2.alarm.api.AlarmApi
import hu.simplexion.z2.service.ServiceImpl

class AlarmImpl : AlarmApi, ServiceImpl<AlarmImpl> {

    companion object {
        val alarmImpl = AlarmImpl().internal
    }

//    override suspend fun alarm(subject: UUID<*>, message: LocalizedText, ex: Exception) {
//        println("$subject $message")
//        ex.printStackTrace()
//    }
}