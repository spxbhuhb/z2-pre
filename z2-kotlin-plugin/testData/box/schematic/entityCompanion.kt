package foo.bar

import hu.simplexion.z2.schematic.SchematicCompanion
import hu.simplexion.z2.schematic.entity.SchematicEntity
import hu.simplexion.z2.schematic.entity.SchematicEntityCompanion

class EntityNoCompanion : SchematicEntity<EntityNoCompanion>() {
    override var uuid by self<EntityNoCompanion>()
    override var name by string()
}

class EntityNoSuperType : SchematicEntity<EntityNoSuperType>() {
    override var uuid by self<EntityNoSuperType>()
    override var name by string()

    companion object {
        val a = 12
    }
}

class CompanionWithSchematicSupertype : SchematicEntity<CompanionWithSchematicSupertype>() {
    override var uuid by self<CompanionWithSchematicSupertype>()
    override var name by string()

    companion object : SchematicCompanion<CompanionWithSchematicSupertype> {
        val a = 23
    }
}

class CompanionWithEntitySupertype : SchematicEntity<CompanionWithEntitySupertype>() {
    override var uuid by self<CompanionWithEntitySupertype>()
    override var name by string()

    companion object : SchematicEntityCompanion<CompanionWithEntitySupertype> {
        val a = 45
    }
}

@Suppress("USELESS_IS_CHECK")
fun box(): String {
    if (EntityNoCompanion !is SchematicEntityCompanion<*>) return "Fail: EntityNoCompanion is not SchematicEntityCompanion"
    if (EntityNoCompanion.schematicFqName != "foo.bar.EntityNoCompanion") return "Fail: EntityNoCompanion.schematicFqName"

    if (EntityNoSuperType !is SchematicEntityCompanion<*>) return "Fail: EntityNoSuperType is not SchematicEntityCompanion"
    if (EntityNoSuperType.schematicFqName != "foo.bar.EntityNoSuperType") return "Fail: EntityNoSuperType.schematicFqName"
    if (EntityNoSuperType.a != 12) return "Fail: EntityNoSuperType.a != 12"

    if (CompanionWithSchematicSupertype !is SchematicEntityCompanion<*>) return "Fail: CompanionWithSchematicSupertype is not SchematicEntityCompanion"
    if (CompanionWithSchematicSupertype.schematicFqName != "foo.bar.CompanionWithSchematicSupertype") return "Fail: CompanionWithSupertype.schematicFqName"
    if (CompanionWithSchematicSupertype.a != 23) return "Fail: CompanionWithSchematicSupertype.a != 23"

    if (CompanionWithEntitySupertype !is SchematicEntityCompanion<*>) return "Fail: CompanionWithEntitySupertype is not SchematicEntityCompanion"
    if (CompanionWithEntitySupertype.schematicFqName != "foo.bar.CompanionWithEntitySupertype") return "Fail: CompanionWithEntitySupertype.schematicFqName"
    if (CompanionWithEntitySupertype.a != 45) return "Fail: CompanionWithEntitySupertype.a != 45"

    return "OK"
}