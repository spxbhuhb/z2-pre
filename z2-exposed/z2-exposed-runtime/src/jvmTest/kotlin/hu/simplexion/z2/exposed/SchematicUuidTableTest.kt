package hu.simplexion.z2.exposed

import hu.simplexion.z2.commons.util.UUID
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.Test
import kotlin.test.assertEquals

class SchematicUuidTableTest {

    @Test
    fun testMapping() {
        h2Test(TestTable)

        val schematic = TestSchematic().apply {
            booleanField = true
            intField = 12
            stringField = "abc"
            uuidField = UUID()
        }

        transaction {
            schematic.uuid = TestTable.insert(schematic)

            val list = TestTable.list()
            assertEquals(1, list.size)
            assertEquals(schematic.uuid, list.first().uuid)

            schematic.intField = 456
            TestTable.update(schematic.uuid, schematic)

            var readback = TestTable.get(schematic.uuid)
            assertEquals(456, readback.intField)

            TestTable.update(schematic.uuid) {
                it[TestTable.intField] = 789
            }

            readback = TestTable.get(schematic.uuid)
            assertEquals(789, readback.intField)

            TestTable.remove(schematic.uuid)
            assertEquals(0, TestTable.list().size)
        }
    }
}