package hu.simplexion.z2.exposed


object TestTable : SchematicUuidTable<TestSchematic>(
    "test",
    TestSchematic()
) {

    val booleanField = bool("booleanField")
    val intField = integer("intField")
    val stringField = text("stringField")
    val uuidField = uuid("uuidField")

}