package hu.simplexion.z2.history

import hu.simplexion.z2.auth.context.account
import hu.simplexion.z2.auth.context.accountOrNull
import hu.simplexion.z2.auth.model.AccountPrivate
import hu.simplexion.z2.commons.util.UUID
import hu.simplexion.z2.history.model.HistoryFlags
import hu.simplexion.z2.service.runtime.ServiceImpl

fun ServiceImpl.securityHistory(vararg parameters : Any?) {
    historyImpl.add(serviceContext.accountOrNull, HistoryFlags.SECURITY, parameters.joinToString())
}

fun securityHistory(account : UUID<AccountPrivate>, vararg parameters : Any?) {
    historyImpl.add(account, HistoryFlags.SECURITY, parameters.joinToString())
}

fun ServiceImpl.history(vararg parameters : Any?) {
    historyImpl.add(serviceContext.account, HistoryFlags.BUSINESS, parameters.joinToString())
}

fun ServiceImpl.history(createdFor : UUID<*>, vararg parameters : Any?) {
    historyImpl.add(serviceContext.account, createdFor, HistoryFlags.BUSINESS, "text/plain", parameters.joinToString())
}

fun history(account : UUID<AccountPrivate>, vararg parameters : Any?) {
    historyImpl.add(account, HistoryFlags.BUSINESS, parameters.joinToString())
}