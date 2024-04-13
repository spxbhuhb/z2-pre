package foo.bar

import hu.simplexion.z2.schematic.entity.SchematicEntity
import hu.simplexion.z2.schematic.schema.field.NullableReferenceSchemaField
import hu.simplexion.z2.schematic.schema.field.ReferenceListSchemaField
import hu.simplexion.z2.schematic.schema.field.ReferenceSchemaField

class TestEntity : SchematicEntity<TestEntity>() {
    override var uuid by self<TestEntity>()
    override var name by string()

    var child by reference<TestEntity2>()
    var optChild by reference<TestEntity2>().nullable()
    var children by referenceList<TestEntity2>()
}

class TestEntity2 : SchematicEntity<TestEntity2>() {
    override var uuid by self<TestEntity2>()
    override var name by string()
}

fun box(): String {
    val schema = TestEntity().schematicCompanion.schematicSchema

    val child = schema.getField("child")
    if (child !is ReferenceSchemaField<*>) return "fail: child !is ReferenceSchemaField<*>"
    if (child.companion.schematicFqName != "foo.bar.TestEntity2") return "fail: child.companion.schematicFqName != \"foo.bar.TestEntity2\""

    val optChild = schema.getField("optChild")
    if (optChild !is NullableReferenceSchemaField<*>) return "fail: optChild !is NullableReferenceSchemaField<*>"
    if (optChild.companion.schematicFqName != "foo.bar.TestEntity2") return "fail: optChild.companion.schematicFqName != \"foo.bar.TestEntity2\""

    val children = schema.getField("children")
    if (children !is ReferenceListSchemaField<*>) return "fail: children !is ReferenceListSchemaField<*>"
    if (children.itemSchemaField.companion.schematicFqName != "foo.bar.TestEntity2") return "fail: children.companion.schematicFqName != \"foo.bar.TestEntity2\""

    return "OK"
}