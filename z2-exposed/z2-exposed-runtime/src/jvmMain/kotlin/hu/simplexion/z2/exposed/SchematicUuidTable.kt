package hu.simplexion.z2.exposed

import hu.simplexion.z2.commons.util.UUID
import hu.simplexion.z2.schematic.runtime.Schematic
import hu.simplexion.z2.schematic.runtime.schema.SchemaFieldType
import hu.simplexion.z2.schematic.runtime.schema.validation.ValidationFailInfo
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.statements.UpdateBuilder
import org.jetbrains.exposed.sql.statements.UpdateStatement

open class SchematicUuidTable<T : Schematic<T>>(
    name : String,
    val template : T,
    val linked : Boolean = false
) : UUIDTable(name, columnName = "uuid") {

    fun newInstance() =
        template.schematicCompanion.newInstance()

    fun list() : List<T> =
        selectAll().map {
            it.toSchematic(this, newInstance())
        }

    fun list(where: SqlExpressionBuilder.() -> Op<Boolean>) : List<T> =
        select(where).map {
            it.toSchematic(this, newInstance())
        }

    fun get(uuid: UUID<T>) : T =
        select { id eq uuid.jvm }
            .map {
                it.toSchematic(this, newInstance())
            }
            .first()

    fun insert(schematic: T): UUID<T> =
        checkNotNull(
            insert {
                it.fromSchematic(this, schematic)
            }.getOrNull(id)
        ).value.z2()

    fun update(uuid: UUID<T>, schematic: T) {
        update({ id eq uuid.jvm }) {
            it.fromSchematic(this, schematic)
        }
    }

    fun update(uuid: UUID<T>, limit: Int? = null, body: SchematicUuidTable<T>.(UpdateStatement) -> Unit) {
        update({ id eq uuid.jvm }, limit, body)
    }

    fun remove(uuid: UUID<T>) {
        deleteWhere { id eq uuid.jvm }
    }

    fun ResultRow.toSchematic(table: Table, schematic: T) : T {
        val row = this
        val fails = mutableListOf<ValidationFailInfo>()
        val sv = schematic.schematicValues

        for (field in schematic.schematicSchema.fields) {
            val column = table.columns.firstOrNull { it.name == field.name } ?: continue
            val exposedValue = row[column] ?: continue

            when {
                exposedValue is EntityID<*> -> {
                    val idValue = exposedValue.value as java.util.UUID
                    sv[field.name] = UUID<Any>(idValue.mostSignificantBits, idValue.leastSignificantBits)
                }
                field.type == SchemaFieldType.UUID && exposedValue is java.util.UUID -> {
                    sv[field.name] = UUID<Any>(exposedValue.mostSignificantBits, exposedValue.leastSignificantBits)
                }
                else -> {
                    sv[field.name] = field.toTypedValue(exposedValue, fails)
                }
            }
        }

        check(fails.isEmpty()) { "cannot convert the row from $table into: ${schematic::class} ${fails.map { it.message }.joinToString()}" }

        return schematic
    }

    fun UpdateBuilder<*>.fromSchematic(table: Table, schematic: Schematic<*>) {
        val statement = this
        for (field in schematic.schematicSchema.fields) {
            if (!linked && field.name == "id") continue

            @Suppress("UNCHECKED_CAST")
            val column = table.columns.firstOrNull { it.name == field.name } as? Column<Any?> ?: continue
            val value = schematic.schematicValues[field.name]
            if (field.type == SchemaFieldType.UUID) {
                statement[column] = (value as UUID<*>).jvm
            } else {
                statement[column] = value
            }
        }
    }
}