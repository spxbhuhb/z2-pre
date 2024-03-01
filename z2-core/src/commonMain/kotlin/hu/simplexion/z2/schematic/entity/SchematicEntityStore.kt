package hu.simplexion.z2.schematic.entity

import hu.simplexion.z2.schematic.SchematicCompanion
import hu.simplexion.z2.util.UUID
import hu.simplexion.z2.util.placeholder

/**
 * Interface for basic entity store functions, very similar to [Map]. Intended use
 * is for service APIs and implementations:
 *
 * ```
 * class Event : SchematicEntity<Event>() {
 *     override var uuid by self()
 *     override var name by string()
 * }
 *
 * interface EventApi : Service, SchematicEntityStore<Event>
 * ```
 */
interface SchematicEntityStore<T : SchematicEntity<T>> {

    /**
     * The companion object of [T]. Set by the compiler plugin.
     */
    val entityCompanion : SchematicCompanion<T>
        get() = placeholder()

    /**
     * Get a new instance of [T] with all properties initialized to their default value
     * This instance is not in the store yet, call [put] to add it.
     */
    fun new(): T = entityCompanion.newInstance()

    suspend fun put(entity: T): UUID<T>

    suspend fun get(uuid: UUID<T>): T?

    suspend fun remove(uuid: UUID<T>)

    suspend fun values(): List<T>

}