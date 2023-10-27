package hu.simplexion.z2.strictId.impl

import hu.simplexion.z2.commons.util.Lock
import hu.simplexion.z2.commons.util.use
import hu.simplexion.z2.service.ServiceImpl
import hu.simplexion.z2.strictId.api.StrictIdApi
import hu.simplexion.z2.strictId.table.StrictIdTable.Companion.strictIdTable

class StrictIdImpl : StrictIdApi, ServiceImpl<StrictIdImpl> {

    companion object {
        val strictIdImpl = StrictIdImpl().internal

        val strictIdLock = Lock()
    }

    override suspend fun next(key: String): Long {
        return strictIdLock.use {
            val nextValue = strictIdTable.get(key) + 1
            if (nextValue == 0L) {
                strictIdTable.add(key)
                1
            } else {
                strictIdTable.update(key, nextValue)
                nextValue
            }
        }
    }
}