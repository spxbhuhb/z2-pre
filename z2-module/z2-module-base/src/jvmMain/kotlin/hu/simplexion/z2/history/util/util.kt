package hu.simplexion.z2.history.util

import hu.simplexion.z2.auth.context.account
import hu.simplexion.z2.auth.context.accountOrNull
import hu.simplexion.z2.auth.model.AccountPrivate
import hu.simplexion.z2.commons.i18n.LocalizedText
import hu.simplexion.z2.commons.i18n.format
import hu.simplexion.z2.commons.util.UUID
import hu.simplexion.z2.history.impl.HistoryImpl.Companion.historyImpl
import hu.simplexion.z2.history.model.HistoryFlags
import hu.simplexion.z2.service.runtime.ServiceContext
import hu.simplexion.z2.service.runtime.ServiceImpl

fun ServiceImpl<*>.securityHistory(topic: LocalizedText, verb : LocalizedText, subject : UUID<*>, vararg parameters: Any?) {
    historyImpl.add(serviceContext.accountOrNull, HistoryFlags.SECURITY, topic, verb, parameters.joinToString())
}

fun securityHistory(account: UUID<AccountPrivate>, topic: LocalizedText, verb : LocalizedText, vararg parameters: Any?) {
    historyImpl.add(account, HistoryFlags.SECURITY, topic, verb, parameters.joinToString())
}

fun ServiceImpl<*>.history(topic: LocalizedText, verb : LocalizedText, vararg parameters: Any?) {
    historyImpl.add(serviceContext.account, HistoryFlags.BUSINESS, topic, verb, parameters.joinToString())
}

fun ServiceImpl<*>.history(topic: LocalizedText, subject: UUID<*>, verb : LocalizedText, vararg parameters: Any?) {
    historyImpl.add(serviceContext.account, HistoryFlags.BUSINESS, topic, verb, subject, "text/plain", parameters.joinToString())
}

fun history(account: UUID<AccountPrivate>, topic: LocalizedText, verb : LocalizedText, vararg parameters: Any?) {
    historyImpl.add(account, HistoryFlags.BUSINESS, topic, verb, parameters.joinToString())
}

fun ServiceImpl<*>.settingHistory(owner : UUID<AccountPrivate>, topic: LocalizedText, verb : LocalizedText, vararg parameters: Any?) {
    historyImpl.add(serviceContext.accountOrNull, HistoryFlags.SETTING, topic, verb, owner, "text/plain", parameters.joinToString())
}

fun systemHistory(topic: LocalizedText, verb : LocalizedText, subject: UUID<*>, vararg parameters: Pair<LocalizedText, Any?>) {
    // TECHNICAL here is intentional, system history is actually technical history
    history(null, HistoryFlags.TECHNICAL, topic, verb, subject, *parameters)
}

fun ServiceImpl<*>.technicalHistory(topic : LocalizedText, verb : LocalizedText, vararg parameters: Pair<LocalizedText, Any?>) {
    history(serviceContext.accountOrNull, HistoryFlags.TECHNICAL, topic, verb, null, *parameters)
}

fun technicalHistory(serviceContext: ServiceContext?, topic : LocalizedText, verb : LocalizedText, subject: UUID<*>, vararg parameters: Pair<LocalizedText, Any?>) {
    history(serviceContext?.accountOrNull, HistoryFlags.TECHNICAL, topic, verb, subject, *parameters)
}

fun technicalHistory(createdBy: UUID<AccountPrivate>, topic : LocalizedText, verb : LocalizedText, subject: UUID<*>, vararg parameters: Pair<LocalizedText, Any?>) {
    history(createdBy, HistoryFlags.TECHNICAL, topic, verb, subject, *parameters)
}

fun history(account : UUID<AccountPrivate>?, flags : Int, topic: LocalizedText, verb : LocalizedText, subject: UUID<*>?, vararg parameters: Pair<LocalizedText, Any?>) {
    historyImpl.add(account, flags, topic, verb, subject, "text/plain", parameters.format())
}