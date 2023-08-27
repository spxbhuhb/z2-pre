package hu.simplexion.z2.content

import hu.simplexion.z2.content.impl.ContentImpl.Companion.contentImpl
import hu.simplexion.z2.content.table.ContentTable.Companion.contentTable
import hu.simplexion.z2.exposed.implementations
import hu.simplexion.z2.exposed.tables

fun contentJvm() {
    contentCommon()
    tables(contentTable)
    implementations(contentImpl)
}