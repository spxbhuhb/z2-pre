package hu.simplexion.z2.strictId

import hu.simplexion.z2.exposed.implementations
import hu.simplexion.z2.exposed.tables
import hu.simplexion.z2.strictId.impl.StrictIdImpl.Companion.strictIdImpl
import hu.simplexion.z2.strictId.table.StrictIdTable.Companion.strictIdTable

fun strictIdJvm() {
    strictIdCommon()
    tables(strictIdTable)
    implementations(strictIdImpl)
}