package hu.simplexion.z2.adaptive.field.select.demo

import hu.simplexion.z2.browser.html.Z2
import hu.simplexion.z2.schematic.entity.SchematicEntity
import hu.simplexion.z2.schematic.entity.SchematicEntityCompanion
import hu.simplexion.z2.schematic.entity.SchematicEntityStore
import hu.simplexion.z2.services.Service
import hu.simplexion.z2.services.ServiceImpl
import hu.simplexion.z2.services.getService
import hu.simplexion.z2.services.transport.DirectServiceCallTransport
import hu.simplexion.z2.util.UUID

fun Z2.entitySelectDemo() {
//    val fieldValue = FieldValue<UUID<TestEntity>>()
//    val fieldState = FieldState()
//    val fieldConfig = FieldConfig()
//    val selectState = SelectState<TestEntity>()
//    val selectConfig = SelectConfig<UUID<TestEntity>, TestEntity>().apply {
//        remote = true
//        query = SelectQuery { _,_ -> TestEntity.schematicEntityStore.values() }
//        renderer = AbstractDropdownListImpl()
//        optionToValue = { _, option -> option.uuid }
//        valueToString = { field, value -> field.selectState.options.first { it.uuid == value }.name }
//        renderItem = { _, option -> text { option.name } }
//        textFieldRenderer = OutlinedTextImpl()
//    }
//
//    grid("400px 400px", "1fr", gridGap24) {
//        val container = div()
//        SelectField(container, fieldValue, fieldState, fieldConfig, selectState, selectConfig).main()
//    }
}

class TestEntity : SchematicEntity<TestEntity>() {
    override var uuid by self<TestEntity>()
    override var name by string()

    companion object : SchematicEntityCompanion<TestEntity>
}

interface TestApi : Service, SchematicEntityStore<TestEntity>

val testService = getService<TestApi>().also { it.serviceCallTransport = DirectServiceCallTransport(TestImpl()) }

class TestImpl : ServiceImpl<TestImpl>, TestApi {

    companion object {
        val store = mutableMapOf(
            TestEntity().let {
                it.uuid = UUID()
                it.name = "Name of ${it.uuid.toShort()}"
                it.uuid to it
            }
        )
    }

    override suspend fun put(entity: TestEntity): UUID<TestEntity> {
        entity.uuid = UUID()
        store[entity.uuid] = entity
        return entity.uuid
    }

    override suspend fun get(uuid: UUID<TestEntity>): TestEntity? {
        return store[uuid]
    }

    override suspend fun remove(uuid: UUID<TestEntity>) {
        store.remove(uuid)
    }

    override suspend fun values(): List<TestEntity> {
        return store.values.toList()
    }

}