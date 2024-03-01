package foo.bar

import hu.simplexion.z2.schematic.entity.SchematicEntity
import hu.simplexion.z2.schematic.entity.SchematicEntityStore
import hu.simplexion.z2.services.Service
import hu.simplexion.z2.services.getService
import hu.simplexion.z2.util.UUID

class TestEntity : SchematicEntity<TestEntity>() {
    override var uuid by self<TestEntity>()
    override var name by string()
}

interface TestStore : Service, SchematicEntityStore<TestEntity>

@Suppress("RedundantNullableReturnType")
class TestStoreImpl : TestStore {
    override suspend fun put(entity: TestEntity): UUID<TestEntity> = TODO()
    override suspend fun get(uuid: UUID<TestEntity>): TestEntity? = TODO()
    override suspend fun remove(uuid: UUID<TestEntity>) = TODO()
    override suspend fun values(): List<TestEntity> = TODO()
}

fun box(): String {
    val impl = TestStoreImpl()

    if (impl.schematicEntityCompanion != TestEntity().schematicCompanion) return "Fail: impl.schematicEntityCompanion"

    val testService = getService<TestStore>()

    if (testService.schematicEntityCompanion != TestEntity().schematicCompanion) return "Fail: testService.schematicCompanion"

    return "OK"
}