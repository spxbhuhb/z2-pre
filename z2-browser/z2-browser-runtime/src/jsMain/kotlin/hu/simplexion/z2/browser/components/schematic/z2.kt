package hu.simplexion.z2.browser.components.schematic

import hu.simplexion.z2.browser.html.Z2
import hu.simplexion.z2.commons.event.AnonymousEventListener
import hu.simplexion.z2.schematic.runtime.SchematicEvent
import hu.simplexion.z2.schematic.runtime.SchematicNode

fun Z2.attach(schematicNode: SchematicNode, listenerFun : (event : SchematicEvent) -> Unit) {
    AnonymousEventListener(schematicNode.schematicHandle) { listenerFun(it as SchematicEvent) }
        .also {
            listeners += it
            schematicNode.attach(it)
        }
}