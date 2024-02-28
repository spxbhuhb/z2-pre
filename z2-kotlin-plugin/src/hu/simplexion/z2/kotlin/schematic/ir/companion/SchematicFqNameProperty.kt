/*
 * Copyright © 2022-2023, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.z2.kotlin.schematic.ir.companion

import hu.simplexion.z2.kotlin.schematic.SCHEMATIC_FQNAME_PROPERTY
import hu.simplexion.z2.kotlin.schematic.ir.SchematicPluginContext
import hu.simplexion.z2.kotlin.schematic.ir.util.IrBuilder
import org.jetbrains.kotlin.backend.common.lower.DeclarationIrBuilder
import org.jetbrains.kotlin.ir.builders.irBlockBody
import org.jetbrains.kotlin.ir.builders.irReturn
import org.jetbrains.kotlin.ir.util.kotlinFqName
import org.jetbrains.kotlin.ir.util.properties

/**
 * Transform the `schematicFqName` property of the companion. The property
 * may be:
 *
 * - missing, when the companion class is created by the plugin - add
 * - fake override, when the companion is declared but the property is not overridden - convert
 * - override, when there is an actual implementation - do not touch
 */
class SchematicFqNameProperty(
    override val pluginContext: SchematicPluginContext,
    val companionTransform: CompanionTransform,
) : IrBuilder {

    val companionClass = companionTransform.companionClass

    var property = companionClass.properties.first { it.name.identifier == SCHEMATIC_FQNAME_PROPERTY }

    fun build() {
        transform()
        companionTransform.companionSchematicFqNameGetter = checkNotNull(property.getter?.symbol)
    }

    fun transform() {
        check(! property.isVar) { "schematicFqName must be immutable" }

        val getter = requireNotNull(property.getter)

        getter.body = DeclarationIrBuilder(irContext, getter.symbol).irBlockBody {
            + irReturn(
                irConst(companionTransform.classTransform.transformedClass.kotlinFqName.asString())
            )
        }
    }

}
