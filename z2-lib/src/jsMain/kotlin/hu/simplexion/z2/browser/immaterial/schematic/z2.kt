package hu.simplexion.z2.browser.immaterial.schematic

import hu.simplexion.z2.browser.html.Z2
import hu.simplexion.z2.deprecated.event.AnonymousEventListener
import hu.simplexion.z2.schematic.SchematicEvent
import hu.simplexion.z2.schematic.SchematicNode

fun Z2.attachListener(schematicNode: SchematicNode, listenerFun : (event : SchematicEvent) -> Unit) {
    AnonymousEventListener(schematicNode.schematicState.handle) {
        if (it !is SchematicEvent) return@AnonymousEventListener
        listenerFun(it)
    }
        .also {
            listeners += it
            schematicNode.attach(it)
        }
}

fun Z2.attach(schematicNode: SchematicNode, renderFun : Z2.() -> Unit) : Z2 {
    renderFun()
    attachListener(schematicNode) {
        clear()
        renderFun()
    }
    return this
}