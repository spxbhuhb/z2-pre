/*
 * Copyright © 2022-2023, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.z2.devutil.kotlin.ir

import hu.simplexion.z2.schematic.kotlin.ir.DevutilPluginContext
import org.jetbrains.kotlin.backend.common.extensions.IrGenerationExtension
import org.jetbrains.kotlin.backend.common.extensions.IrPluginContext
import org.jetbrains.kotlin.ir.declarations.IrModuleFragment
import org.jetbrains.kotlin.ir.util.companionObject
import org.jetbrains.kotlin.ir.util.dump
import org.jetbrains.kotlin.name.ClassId
import org.jetbrains.kotlin.name.FqName
import org.jetbrains.kotlin.name.Name

internal class DevutilGenerationExtension: IrGenerationExtension {

    override fun generate(moduleFragment: IrModuleFragment, pluginContext: IrPluginContext) {

        DevutilPluginContext(pluginContext).apply {

            val ap = irContext.referenceClass(ClassId(FqName("hu.simplexion.z2.auth.model"), Name.identifier("AccountPrivate")))
            val c = ap!!.owner.companionObject()
            val apc = irContext.referenceClass(ClassId(FqName("hu.simplexion.z2.auth.model"), Name.identifier("AccountPrivate\$Companion")))

            debug("devutil") { "".padEnd(80, '=') }
            debug("devutil") { moduleFragment.dump() }

        }
    }
}

