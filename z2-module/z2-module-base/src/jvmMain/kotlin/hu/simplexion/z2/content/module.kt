package hu.simplexion.z2.content

import hu.simplexion.z2.content.impl.ContentImpl.Companion.contentImpl
import hu.simplexion.z2.content.tables.ContentTable.Companion.contentTable
import hu.simplexion.z2.exposed.implementations
import hu.simplexion.z2.exposed.tables

fun contentJvm() {
    contentCommon()
    tables(contentTable)
    implementations(contentImpl)
}