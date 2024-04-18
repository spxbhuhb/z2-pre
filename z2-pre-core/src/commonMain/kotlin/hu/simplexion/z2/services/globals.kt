package hu.simplexion.z2.services

import hu.simplexion.z2.schematic.entity.SchematicEntity
import hu.simplexion.z2.schematic.entity.SchematicEntityCompanion
import hu.simplexion.z2.schematic.entity.SchematicEntityStore
import hu.simplexion.z2.services.factory.BasicServiceImplFactory
import hu.simplexion.z2.services.factory.ServiceImplFactory
import hu.simplexion.z2.services.transport.LocalServiceCallTransport
import hu.simplexion.z2.services.transport.ServiceCallTransport
import hu.simplexion.z2.util.UUID

var defaultServiceCallTransport: ServiceCallTransport = LocalServiceCallTransport()

val defaultServiceImplFactory: ServiceImplFactory = BasicServiceImplFactory()

/**
 * Get a service consumer for the interface, specified by the type parameter.
 *
 * **You should NOT pass the [consumer] parameter! It is set by the compiler plugin.**
 *
 * ```kotlin
 * val clicks = getService<ClickApi>()
 * ```
 */
fun <T : Service> getService(consumer: T? = null): T {
    checkNotNull(consumer)
    if (consumer is SchematicEntityStore<*>) {
        consumer.schematicEntityCompanion.setSchematicEntityStore(consumer)
    }
    return consumer
}

/**
 * This function is here `getService` has no way to do the proper cast without
 * the type information.
 */
private fun <T : SchematicEntity<T>> SchematicEntityCompanion<T>.setSchematicEntityStore(store : SchematicEntityStore<*>) {
    check(store.schematicEntityCompanion == this)
    @Suppress("UNCHECKED_CAST") // the check above ensures that the cast is right
    schematicEntityStore = store as SchematicEntityStore<T>
}

/**
 * Get a typed data instance fom the service context.
 *
 * @throws  ClassCastException  If the instance is not of the class [T].
 */
inline operator fun <reified T> ServiceContext.get(uuid: UUID<T>): T? {
    return data.let { it[uuid] } as? T
}

/**
 * Put a data instance into the service context.
 */
operator fun <T> ServiceContext.set(uuid: UUID<T>, value: T) {
    data.let { it[uuid] = value }
}