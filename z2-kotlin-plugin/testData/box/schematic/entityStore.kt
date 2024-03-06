package foo.bar

import hu.simplexion.z2.schematic.entity.SchematicEntity
import hu.simplexion.z2.schematic.entity.SchematicEntityStore
import hu.simplexion.z2.services.getService
import hu.simplexion.z2.services.Service
import hu.simplexion.z2.services.ServiceImpl
import hu.simplexion.z2.services.defaultServiceImplFactory
import hu.simplexion.z2.services.transport.DirectServiceCallTransport
import hu.simplexion.z2.util.UUID
import kotlinx.coroutines.runBlocking

class TestEntity : SchematicEntity<TestEntity>() {
    override var uuid by self<TestEntity>()
    override var name by string()
}

interface TestApi : Service, SchematicEntityStore<TestEntity>

val testService = getService<TestApi>().also { it.serviceCallTransport = DirectServiceCallTransport(TestImpl()) }

class TestImpl : ServiceImpl<TestImpl>, TestApi {

    companion object {
        val store = mutableMapOf<UUID<TestEntity>, TestEntity>()
    }

    override suspend fun put(entity: TestEntity): UUID<TestEntity> {
        entity.uuid = UUID()
        store[entity.uuid] = entity
        println("entityStore.TestImpl.put ${entity.uuid}")
        return entity.uuid
    }

    override suspend fun get(uuid: UUID<TestEntity>): TestEntity? {
        println("entityStore.TestImpl.get ${uuid}")
        return store[uuid]
    }

    override suspend fun remove(uuid: UUID<TestEntity>) {
        store.remove(uuid)
    }

    override suspend fun values(): List<TestEntity> {
        return store.values.toList()
    }
}

fun box(): String {
    val impl = TestImpl().also { defaultServiceImplFactory += it }

    if (impl.schematicEntityCompanion != TestEntity().schematicCompanion) return "Fail: impl.schematicEntityCompanion"

    val testService = getService<TestApi>()

    if (testService.schematicEntityCompanion != TestEntity().schematicCompanion) return "Fail: testService.schematicCompanion"

    val testEntity = TestEntity().also { it.uuid = UUID() }.also { it.name = "hello" }

    val readBack = runBlocking {
        testEntity.uuid = testService.put(testEntity)
        testService.get(testEntity.uuid)
    }

    if (readBack == null) return "Fail: readBack"
    if (readBack.uuid != testEntity.uuid) return "Fail: testEntity.uuid"
    if (readBack.name != testEntity.name) return "Fail: testEntity.name"

    val values = runBlocking { testService.values() }

    if (values.isEmpty()) return "Fail: values"
    if (values[0].uuid != testEntity.uuid) return "Fail: testEntity.uuid"
    if (values[0].name != testEntity.name) return "Fail: testEntity.name"

    return "OK"
}