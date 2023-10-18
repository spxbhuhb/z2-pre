package hu.simplexion.z2.history.util

import hu.simplexion.z2.auth.context.principal
import hu.simplexion.z2.auth.context.principalOrNull
import hu.simplexion.z2.auth.model.Principal
import hu.simplexion.z2.commons.util.UUID
import hu.simplexion.z2.history.impl.HistoryImpl.Companion.historyImpl
import hu.simplexion.z2.history.model.HistoryFlags
import hu.simplexion.z2.localization.format
import hu.simplexion.z2.localization.text.LocalizedText
import hu.simplexion.z2.service.ServiceContext
import hu.simplexion.z2.service.ServiceImpl

fun ServiceImpl<*>.securityHistory(topic: LocalizedText, verb : LocalizedText, subject : UUID<*>, vararg parameters: Any?) {
    historyImpl.add(serviceContext.principalOrNull, HistoryFlags.SECURITY, topic, verb, parameters.joinToString())
}

fun securityHistory(principal: UUID<Principal>, topic: LocalizedText, verb : LocalizedText, vararg parameters: Any?) {
    historyImpl.add(principal, HistoryFlags.SECURITY, topic, verb, parameters.joinToString())
}

fun ServiceImpl<*>.history(topic: LocalizedText, verb : LocalizedText, vararg parameters: Any?) {
    historyImpl.add(serviceContext.principal, HistoryFlags.BUSINESS, topic, verb, parameters.joinToString())
}

fun ServiceImpl<*>.history(topic: LocalizedText, subject: UUID<*>, verb : LocalizedText, vararg parameters: Any?) {
    historyImpl.add(serviceContext.principal, HistoryFlags.BUSINESS, topic, verb, subject, "text/plain", parameters.joinToString())
}

fun history(principal: UUID<Principal>, topic: LocalizedText, verb : LocalizedText, vararg parameters: Any?) {
    historyImpl.add(principal, HistoryFlags.BUSINESS, topic, verb, parameters.joinToString())
}

fun ServiceImpl<*>.settingHistory(owner : UUID<Principal>, topic: LocalizedText, verb : LocalizedText, vararg parameters: Any?) {
    historyImpl.add(serviceContext.principalOrNull, HistoryFlags.SETTING, topic, verb, owner, "text/plain", parameters.joinToString())
}

fun systemHistory(topic: LocalizedText, verb : LocalizedText, subject: UUID<*>, vararg parameters: Pair<LocalizedText, Any?>) {
    // TECHNICAL here is intentional, system history is actually technical history
    history(null, HistoryFlags.TECHNICAL, topic, verb, subject, *parameters)
}

fun ServiceImpl<*>.technicalHistory(topic : LocalizedText, verb : LocalizedText, vararg parameters: Pair<LocalizedText, Any?>) {
    history(serviceContext.principalOrNull, HistoryFlags.TECHNICAL, topic, verb, null, *parameters)
}

fun technicalHistory(serviceContext: ServiceContext?, topic : LocalizedText, verb : LocalizedText, subject: UUID<*>, vararg parameters: Pair<LocalizedText, Any?>) {
    history(serviceContext?.principalOrNull, HistoryFlags.TECHNICAL, topic, verb, subject, *parameters)
}

fun technicalHistory(createdBy: UUID<Principal>, topic : LocalizedText, verb : LocalizedText, subject: UUID<*>, vararg parameters: Pair<LocalizedText, Any?>) {
    history(createdBy, HistoryFlags.TECHNICAL, topic, verb, subject, *parameters)
}

fun history(principal : UUID<Principal>?, flags : Int, topic: LocalizedText, verb : LocalizedText, subject: UUID<*>?, vararg parameters: Pair<LocalizedText, Any?>) {
    historyImpl.add(principal, flags, topic, verb, subject, "text/plain", parameters.format())
}