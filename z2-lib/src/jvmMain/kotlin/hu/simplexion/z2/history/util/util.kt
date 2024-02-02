package hu.simplexion.z2.history.util

import hu.simplexion.z2.auth.context.principal
import hu.simplexion.z2.auth.context.principalOrNull
import hu.simplexion.z2.auth.model.Principal
import hu.simplexion.z2.util.UUID
import hu.simplexion.z2.history.impl.HistoryImpl.Companion.historyImpl
import hu.simplexion.z2.history.model.HistoryFlags
import hu.simplexion.z2.localization.format
import hu.simplexion.z2.localization.text.LocalizedText
import hu.simplexion.z2.services.ServiceContext
import hu.simplexion.z2.services.ServiceImpl

fun ServiceImpl<*>.securityHistory(topic: LocalizedText, verb : LocalizedText, subject : UUID<*>, vararg parameters: Any?) {
    historyImpl.add(serviceContext.principalOrNull, HistoryFlags.SECURITY, topic, verb, parameters.joinToString())
}

fun securityHistory(principal: UUID<Principal>?, topic: LocalizedText, verb : LocalizedText, vararg parameters: Any?) {
    historyImpl.add(principal, HistoryFlags.SECURITY, topic, verb, parameters.joinToString())
}

fun ServiceImpl<*>.businessHistory(topic: LocalizedText, verb : LocalizedText, vararg parameters: Any?) {
    historyImpl.add(serviceContext.principal, HistoryFlags.BUSINESS, topic, verb, parameters.joinToString())
}

fun ServiceImpl<*>.businessHistory(topic: LocalizedText, subject: UUID<*>, verb : LocalizedText, vararg parameters: Any?) {
    historyImpl.add(serviceContext.principal, HistoryFlags.BUSINESS, topic, verb, subject, "text/plain", parameters.joinToString())
}

fun businessHistory(principal: UUID<Principal>, topic: LocalizedText, verb : LocalizedText, vararg parameters: Any?) {
    historyImpl.add(principal, HistoryFlags.BUSINESS, topic, verb, parameters.joinToString())
}

fun ServiceImpl<*>.settingHistory(owner : UUID<Principal>, topic: LocalizedText, verb : LocalizedText, vararg parameters: Any?) {
    historyImpl.add(serviceContext.principalOrNull, HistoryFlags.SETTING, topic, verb, owner, "text/plain", parameters.joinToString())
}

fun technicalHistory(topic: LocalizedText, verb : LocalizedText, subject: UUID<*>, vararg parameters: Pair<LocalizedText, Any?>) {
    businessHistory(null, HistoryFlags.TECHNICAL, topic, verb, subject, *parameters)
}

fun technicalHistory(topic: LocalizedText, verb : LocalizedText, vararg parameters: Any?) {
    historyImpl.add(null, HistoryFlags.TECHNICAL, topic, verb, null, "text/plain", parameters.joinToString())
}

fun ServiceImpl<*>.technicalHistory(topic : LocalizedText, verb : LocalizedText, vararg parameters: Pair<LocalizedText, Any?>) {
    businessHistory(serviceContext.principalOrNull, HistoryFlags.TECHNICAL, topic, verb, null, *parameters)
}

fun technicalHistory(serviceContext: ServiceContext, topic : LocalizedText, verb : LocalizedText, subject: UUID<*>, vararg parameters: Pair<LocalizedText, Any?>) {
    businessHistory(serviceContext.principalOrNull, HistoryFlags.TECHNICAL, topic, verb, subject, *parameters)
}

fun technicalHistory(createdBy: UUID<Principal>, topic : LocalizedText, verb : LocalizedText, subject: UUID<*>, vararg parameters: Pair<LocalizedText, Any?>) {
    businessHistory(createdBy, HistoryFlags.TECHNICAL, topic, verb, subject, *parameters)
}

fun businessHistory(principal : UUID<Principal>?, flags : Int, topic: LocalizedText, verb : LocalizedText, subject: UUID<*>?, vararg parameters: Pair<LocalizedText, Any?>) {
    historyImpl.add(principal, flags, topic, verb, subject, "text/plain", parameters.format())
}