package hu.simplexion.z2.strictId.table

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.update

open class StrictIdTable : Table(
    "z2_strict_id"
) {

    companion object {
        val strictIdTable = StrictIdTable()
    }

    val key = varchar("key", 200).uniqueIndex()
    val value = long("value")

    fun add(inKey: String) {
        insert {
            it[key] = inKey
            it[value] = 1
        }
    }

    fun update(inKey: String, inValue: Long) {
        update({ key eq inKey }) {
            it[value] = inValue
        }
    }

    fun get(inKey: String): Long =
        select { key eq inKey }.singleOrNull()?.let { it[value] } ?: -1

}