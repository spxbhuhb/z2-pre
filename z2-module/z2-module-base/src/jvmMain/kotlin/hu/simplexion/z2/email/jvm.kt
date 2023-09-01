package hu.simplexion.z2.email

import hu.simplexion.z2.email.impl.EmailImpl.Companion.emailImpl
import hu.simplexion.z2.email.table.EmailTable.Companion.emailTable
import hu.simplexion.z2.exposed.implementations
import hu.simplexion.z2.exposed.tables

fun emailJvm() {
    emailCommon()
    tables(emailTable)
    implementations(emailImpl)
}