/*
 * Copyright © 2022-2023, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.z2.kotlin.ir.service.impl

import hu.simplexion.z2.kotlin.ir.service.ServicePluginContext
import hu.simplexion.z2.kotlin.ir.service.util.IrBuilder
import org.jetbrains.kotlin.descriptors.DescriptorVisibilities
import org.jetbrains.kotlin.ir.builders.declarations.buildField
import org.jetbrains.kotlin.ir.declarations.IrDeclarationOrigin
import org.jetbrains.kotlin.ir.declarations.IrProperty
import org.jetbrains.kotlin.ir.expressions.IrExpressionBody
import org.jetbrains.kotlin.ir.types.makeNullable

class ServiceContextPropertyTransform(
    override val pluginContext: ServicePluginContext,
    val implClassTransform: ImplClassTransform,
    var property: IrProperty
) : IrBuilder {

    fun build() {
        if (! property.isFakeOverride) return

        property.isFakeOverride = false
        property.origin = IrDeclarationOrigin.DEFINED

        property.backingField = irFactory.buildField {
            name = property.name
            type = pluginContext.serviceContextType.makeNullable()
            origin = IrDeclarationOrigin.PROPERTY_BACKING_FIELD
            visibility = DescriptorVisibilities.PRIVATE
        }.apply {
            parent = property.parent
            initializer = buildInitializer()
            correspondingPropertySymbol = property.symbol
        }

        transformGetter(implClassTransform.transformedClass, property.getter!!, property.backingField!!)
    }

    fun buildInitializer() : IrExpressionBody =
        irFactory.createExpressionBody(irGet(implClassTransform.constructor.valueParameters.first()))

}