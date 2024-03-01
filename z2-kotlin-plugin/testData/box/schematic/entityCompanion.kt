package foo.bar

import hu.simplexion.z2.schematic.SchematicCompanion
import hu.simplexion.z2.schematic.entity.SchematicEntity
import hu.simplexion.z2.schematic.entity.SchematicEntityCompanion

class NoCompanion : SchematicEntity<NoCompanion>() {
    override var uuid by self<NoCompanion>()
    override var name by string()
}

class NoSupertype : SchematicEntity<NoSupertype>() {
    override var uuid by self<NoSupertype>()
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
    if (NoCompanion !is SchematicEntityCompanion<*>) return "Fail: NoCompanion is not SchematicEntityCompanion"
    if (NoCompanion.schematicFqName != "foo.bar.NoCompanion") return "Fail: NoCompanion.schematicFqName"

    if (NoSupertype !is SchematicEntityCompanion<*>) return "Fail: NoSupertype is not SchematicEntityCompanion"
    if (NoSupertype.schematicFqName != "foo.bar.NoSupertype") return "Fail: NoSupertype.schematicFqName"
    if (NoSupertype.a != 12) return "Fail: NoSupertype.a != 12"

    if (CompanionWithSchematicSupertype !is SchematicEntityCompanion<*>) return "Fail: CompanionWithSchematicSupertype is not SchematicEntityCompanion"
    if (CompanionWithSchematicSupertype.schematicFqName != "foo.bar.CompanionWithSchematicSupertype") return "Fail: CompanionWithSupertype.schematicFqName"
    if (CompanionWithSchematicSupertype.a != 23) return "Fail: CompanionWithSchematicSupertype.a != 23"

    if (CompanionWithEntitySupertype !is SchematicEntityCompanion<*>) return "Fail: CompanionWithEntitySupertype is not SchematicEntityCompanion"
    if (CompanionWithEntitySupertype.schematicFqName != "foo.bar.CompanionWithEntitySupertype") return "Fail: CompanionWithEntitySupertype.schematicFqName"
    if (CompanionWithEntitySupertype.a != 45) return "Fail: CompanionWithEntitySupertype.a != 45"

    return "OK"
}