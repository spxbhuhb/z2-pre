package hu.simplexion.z2.schematic.runtime

import hu.simplexion.z2.commons.event.EventCentral
import hu.simplexion.z2.commons.event.Z2EventListener
import hu.simplexion.z2.commons.util.Z2Handle
import hu.simplexion.z2.schematic.runtime.schema.SchemaField

/**
 * Schematic nodes may be organized into a tree like DOM. This provides
 * the structural context for the events they might need.
 */
interface SchematicNode {

    /**
     * The unique handle of this node. All instances must use
     * `nextHandle` to get their own handle.
     */
    val schematicHandle : Z2Handle

    var schematicParent : SchematicNode?

    /**
     * When not null the schematic will create events on data changes
     * and also run validation after each data change. Incremented and
     * decremented by [attach] and [detach] respectively.
     */
    var schematicListenerCount : Int

    fun fireEvent(field: SchemaField<*>)

    /**
     * Attach a listener to this schematic. Calls [EventCentral.attach]
     * and increments [schematicListenerCount].
     *
     * This method is **NOT** thread safe.
     */
    fun attach(listener: Z2EventListener) {
        EventCentral.attach(schematicHandle, listener)
        schematicListenerCount ++
    }

    /**
     * Detach a previously attached listener and decrement [schematicListenerCount].
     *
     * This method is **NOT** thread safe.
     */
    fun detach(listener: Z2EventListener) {
        schematicListenerCount --
        EventCentral.detach(schematicHandle, listener)
    }
}

