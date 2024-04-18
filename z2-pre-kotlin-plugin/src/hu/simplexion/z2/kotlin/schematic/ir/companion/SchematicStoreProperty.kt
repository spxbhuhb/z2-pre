/*
 * Copyright © 2022-2023, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.z2.kotlin.schematic.ir.companion

import hu.simplexion.z2.kotlin.schematic.Names
import hu.simplexion.z2.kotlin.schematic.ir.SchematicPluginContext
import hu.simplexion.z2.kotlin.schematic.ir.util.IrBuilder
import org.jetbrains.kotlin.ir.util.properties

/**
 * Transform the `schematicStore` property of the companion.
 */
class SchematicStoreProperty(
    override val pluginContext: SchematicPluginContext,
    val companionTransform: CompanionTransform,
) : IrBuilder {

    val companionClass = companionTransform.companionClass

    val property = companionClass.properties.firstOrNull { it.name == Names.SCHEMATIC_ENTITY_STORE_PROPERTY }

    fun build() {
        if (property == null) return // not an entity

        check(property.isVar) { "schematicStore must be mutable" }

        property.isLateinit = true
    }

}
