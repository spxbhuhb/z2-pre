package hu.simplexion.z2.exposed

import hu.simplexion.z2.commons.util.UUID
import hu.simplexion.z2.schematic.Schematic
import hu.simplexion.z2.schematic.schema.SchemaFieldType
import hu.simplexion.z2.schematic.schema.field.DecimalSchemaFieldDefault
import hu.simplexion.z2.schematic.schema.validation.ValidationFailInfo
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IdTable
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.SqlExpressionBuilder.isNull
import org.jetbrains.exposed.sql.SqlExpressionBuilder.wrap
import org.jetbrains.exposed.sql.statements.UpdateBuilder
import org.jetbrains.exposed.sql.statements.UpdateStatement
import java.math.BigDecimal

open class SchematicUuidTable<T : Schematic<T>>(
    name: String,
    val template: T,
    val autoGenerateId: Boolean = true,
) : UUIDTable(name, columnName = "uuid") {

    fun newInstance() =
        template.schematicCompanion.newInstance()

    @Deprecated("use query instead", ReplaceWith("query()"))
    fun list(limit: Int? = null, offset: Long? = null): List<T> =
        query(limit, offset)

    fun query(limit: Int? = null, offset: Long? = null): List<T> {
        var statement = selectAll()

        if (limit != null) {
            statement = if (offset != null) statement.limit(limit, offset) else statement.limit(limit)
        }

        return statement.map {
            it.toSchematic(this, newInstance())
        }
    }

    @Deprecated("use query instead", ReplaceWith("query"))
    fun list(limit: Int? = null, offset: Long? = null, where: SqlExpressionBuilder.() -> Op<Boolean>): List<T> =
        query(limit, offset, where)

    fun query(limit: Int? = null, offset: Long? = null, where: SqlExpressionBuilder.() -> Op<Boolean>): List<T> {

        var statement = select(where)

        if (limit != null) {
            statement = if (offset != null) statement.limit(limit, offset) else statement.limit(limit)
        }

        return statement.map {
            it.toSchematic(this, newInstance())
        }
    }

    operator fun get(uuid: UUID<T>): T =
        select { id eq uuid.jvm }
            .map {
                it.toSchematic(this, newInstance())
            }
            .first()

    operator fun plusAssign(schematic: T) {
        insert(schematic)
    }

    fun insert(schematic: T): UUID<T> {
        val uuid: UUID<T> = checkNotNull(
            insert {
                it.fromSchematic(this, schematic, autoGenerateId)
            }.getOrNull(id)
        ).value.z2()

        return if (autoGenerateId) {
            schematic.schematicSet("uuid", uuid)
            return uuid
        } else {
            @Suppress("UNCHECKED_CAST") // FIXME make a SchematicEntity class with a uuid field, or something
            schematic.schematicGet("uuid") as UUID<T>
        }
    }

    fun update(uuid: UUID<T>, schematic: T) {
        update({ id eq uuid.jvm }) {
            it.fromSchematic(this, schematic, true)
        }
    }

    fun update(uuid: UUID<T>, limit: Int? = null, body: SchematicUuidTable<T>.(UpdateStatement) -> Unit) {
        update({ id eq uuid.jvm }, limit, body)
    }

    operator fun minusAssign(uuid: UUID<T>) {
        remove(uuid)
    }

    fun remove(uuid: UUID<T>) {
        deleteWhere { id eq uuid.jvm }
    }

    fun ResultRow.toSchematic(table: Table, schematic: T): T {
        val row = this
        val fails = mutableListOf<ValidationFailInfo>()
        val sv = schematic.schematicValues

        for (field in schematic.schematicSchema.fields) {
            val column = table.columns.firstOrNull { it.name == field.name } ?: continue
            val exposedValue = row[column] ?: continue

            sv[field.name] = when {
                exposedValue is EntityID<*> -> {
                    val idValue = exposedValue.value as java.util.UUID
                    UUID<Any>(idValue.mostSignificantBits, idValue.leastSignificantBits)
                }

                field.type == SchemaFieldType.UUID && exposedValue is java.util.UUID -> {
                    UUID<Any>(exposedValue.mostSignificantBits, exposedValue.leastSignificantBits)
                }

                field.type == SchemaFieldType.Decimal -> {
                    (exposedValue as BigDecimal)
                    exposedValue.movePointRight(exposedValue.scale()).toLong()
                }

                else -> {
                    field.toTypedValue(exposedValue, fails)
                }
            }
        }

        check(fails.isEmpty()) { "cannot convert the row from $table into: ${schematic::class} ${fails.map { it.message }.joinToString()}" }

        return schematic
    }

    fun UpdateBuilder<*>.fromSchematic(table: Table, schematic: Schematic<*>, skipId: Boolean) {
        val statement = this
        for (field in schematic.schematicSchema.fields) {
            // this let the SQL generate the uuid instead using the one in the schematic
            // on the long run we'll probably need a version which uses the supplied id but
            // it is OK for now.
            if (field.name == "uuid" && skipId) continue

            @Suppress("UNCHECKED_CAST")
            val column = table.columns.firstOrNull { it.name == field.name } as? Column<Any?> ?: continue
            val value = schematic.schematicValues[field.name]

            statement[column] = when (field.type) {
                SchemaFieldType.Decimal -> value?.let { BigDecimal.valueOf(it as Long, (field as DecimalSchemaFieldDefault).scale) }
                SchemaFieldType.UUID -> (value as? UUID<*>)?.jvm
                else -> value
            }
        }
    }

    infix fun ExpressionWithColumnType<EntityID<java.util.UUID>>.eq(t: UUID<*>?): Op<Boolean> {
        if (t == null) return isNull()

        @Suppress("UNCHECKED_CAST")
        val table = (columnType as EntityIDColumnType<*>).idColumn.table as IdTable<java.util.UUID>
        val entityID = EntityID(t.jvm, table)
        return EqOp(this, wrap(entityID))
    }

}