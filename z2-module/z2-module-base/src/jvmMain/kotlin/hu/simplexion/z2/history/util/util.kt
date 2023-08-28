package hu.simplexion.z2.history.util

import hu.simplexion.z2.auth.context.account
import hu.simplexion.z2.auth.context.accountOrNull
import hu.simplexion.z2.auth.model.AccountPrivate
import hu.simplexion.z2.commons.i18n.LocalizedText
import hu.simplexion.z2.commons.util.UUID
import hu.simplexion.z2.history.impl.HistoryImpl.Companion.historyImpl
import hu.simplexion.z2.history.model.HistoryFlags
import hu.simplexion.z2.service.runtime.ServiceContext
import hu.simplexion.z2.service.runtime.ServiceImpl

fun ServiceImpl<*>.securityHistory(topic: LocalizedText, vararg parameters: Any?) {
    historyImpl.add(serviceContext.accountOrNull, HistoryFlags.SECURITY, topic.toString(), parameters.joinToString())
}

fun securityHistory(account: UUID<AccountPrivate>, topic: LocalizedText, vararg parameters: Any?) {
    historyImpl.add(account, HistoryFlags.SECURITY, topic.toString(), parameters.joinToString())
}

fun ServiceImpl<*>.history(topic: LocalizedText, vararg parameters: Any?) {
    historyImpl.add(serviceContext.account, HistoryFlags.BUSINESS, topic.toString(), parameters.joinToString())
}

fun ServiceImpl<*>.history(topic: LocalizedText, subject: UUID<*>, vararg parameters: Any?) {
    historyImpl.add(serviceContext.account, HistoryFlags.BUSINESS, topic.toString(), subject, "text/plain", parameters.joinToString())
}

fun history(account: UUID<AccountPrivate>, topic: LocalizedText, vararg parameters: Any?) {
    historyImpl.add(account, HistoryFlags.BUSINESS, topic.toString(), parameters.joinToString())
}

fun ServiceImpl<*>.settingHistory(owner : UUID<AccountPrivate>, topic: LocalizedText, vararg parameters: Any?) {
    historyImpl.add(serviceContext.accountOrNull, HistoryFlags.SETTING, topic.toString(), owner, "text/plain", parameters.joinToString())
}

fun systemHistory(topic: LocalizedText, subject: UUID<*>, vararg parameters: Pair<LocalizedText, Any?>) {
    technicalHistory(UUID.nil(), topic, subject, *parameters)
}

fun ServiceImpl<*>.technicalHistory(topic : LocalizedText, vararg parameters: Pair<LocalizedText, Any?>) {
    technicalHistory(serviceContext.accountOrNull, topic, null, *parameters)
}

fun technicalHistory(serviceContext: ServiceContext?, topic : LocalizedText, subject: UUID<*>, vararg parameters: Pair<LocalizedText, Any?>) {
    technicalHistory(serviceContext?.accountOrNull, topic, subject, *parameters)
}

fun technicalHistory(account : UUID<AccountPrivate>?, topic: LocalizedText, subject: UUID<*>?, vararg parameters: Pair<LocalizedText, Any?>) {
    val formatted = mutableListOf<String>()
    for (parameter in parameters) {
        formatted += parameter.first.toString() + "=" + parameter.second
    }
    historyImpl.add(account, HistoryFlags.TECHNICAL, topic.toString(), subject, "text/plain", formatted.joinToString())
}