/*
 * Copyright © 2022-2023, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.z2.services.kotlin.ir

import hu.simplexion.z2.kotlin.ir.services.ServiceModuleTransform
import hu.simplexion.z2.kotlin.ir.services.ServicePluginContext
import hu.simplexion.z2.kotlin.ir.services.consumer.GetConsumerTransform
import hu.simplexion.z2.kotlin.ir.services.proto.ProtoCompanionVisitor
import org.jetbrains.kotlin.backend.common.extensions.IrGenerationExtension
import org.jetbrains.kotlin.backend.common.extensions.IrPluginContext
import org.jetbrains.kotlin.ir.declarations.IrModuleFragment

internal class ServiceGenerationExtension : IrGenerationExtension {

    override fun generate(moduleFragment: IrModuleFragment, pluginContext: IrPluginContext) {

        ServicePluginContext(pluginContext).apply {

//            debug("service") { "====  START  ==".padEnd(80, '=') }

            // order is important here
            moduleFragment.accept(ProtoCompanionVisitor(this, protoCache), null)
            moduleFragment.accept(ServiceModuleTransform(this), null)
            moduleFragment.accept(GetConsumerTransform(this), null)

//            debug("service") { moduleFragment.dump() }
//            debug("service") { "====  END  ====".padEnd(80, '=') }
        }
    }
}

