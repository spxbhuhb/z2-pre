package hu.simplexion.z2.schematic

import hu.simplexion.z2.deprecated.event.EventCentral
import hu.simplexion.z2.deprecated.event.Z2EventListener
import hu.simplexion.z2.schematic.schema.SchemaField

/**
 * Schematic nodes may be organized into a tree like DOM. This provides
 * the structural context for the events they might need.
 */
interface SchematicNode {

    val schematicState: SchematicState

    fun fireEvent(field: SchemaField<*>)

    /**
     * Attach a listener to this schematic. Calls [EventCentral.attach]
     * and increments [schematicState].listenerCount.
     *
     * This method is **NOT** thread safe.
     */
    fun attach(listener: Z2EventListener) {
        EventCentral.attach(listener)
        schematicState.listenerCount++
    }

    /**
     * Detach a previously attached listener and decrement [schematicState].listenerCount.
     *
     * This method is **NOT** thread safe.
     */
    fun detach(listener: Z2EventListener) {
        schematicState.listenerCount--
        EventCentral.detach(listener)
    }
}

