/*
 * Copyright © 2022-2023, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.z2.kotlin.services.ir

import hu.simplexion.z2.kotlin.Z2Options
import hu.simplexion.z2.kotlin.services.ir.consumer.GetConsumerTransform
import hu.simplexion.z2.kotlin.services.ir.proto.ProtoCompanionVisitor
import org.jetbrains.kotlin.backend.common.extensions.IrGenerationExtension
import org.jetbrains.kotlin.backend.common.extensions.IrPluginContext
import org.jetbrains.kotlin.ir.declarations.IrModuleFragment

class ServicesGenerationExtension(
    val options: Z2Options
) : IrGenerationExtension {

    override fun generate(moduleFragment: IrModuleFragment, pluginContext: IrPluginContext) {
        ServicesPluginContext(pluginContext).apply {
            moduleFragment.accept(ProtoCompanionVisitor(this, protoCache), null)
            moduleFragment.accept(ServicesModuleTransform(this), null)
            moduleFragment.accept(GetConsumerTransform(this), null)
        }
    }

}