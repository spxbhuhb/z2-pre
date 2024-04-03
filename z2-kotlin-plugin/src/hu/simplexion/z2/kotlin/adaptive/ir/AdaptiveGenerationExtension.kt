/*
 * Copyright © 2022-2023, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.z2.kotlin.adaptive.ir

import hu.simplexion.z2.kotlin.Z2Options
import hu.simplexion.z2.kotlin.adaptive.ir.arm2ir.ArmClassBuilder
import hu.simplexion.z2.kotlin.adaptive.ir.arm2ir.ArmEntryPointBuilder
import hu.simplexion.z2.kotlin.adaptive.ir.ir2arm.EntryPointTransform
import hu.simplexion.z2.kotlin.adaptive.ir.ir2arm.OriginalFunctionTransform
import org.jetbrains.kotlin.backend.common.extensions.IrGenerationExtension
import org.jetbrains.kotlin.backend.common.extensions.IrPluginContext
import org.jetbrains.kotlin.ir.declarations.IrModuleFragment
import org.jetbrains.kotlin.ir.util.addChild
import org.jetbrains.kotlin.ir.util.dumpKotlinLike
import org.jetbrains.kotlin.ir.util.file

internal class AdaptiveGenerationExtension(
    val options: Z2Options
) : IrGenerationExtension {

    override fun generate(moduleFragment: IrModuleFragment, pluginContext: IrPluginContext) {

        AdaptivePluginContext(
            pluginContext,
            options,
        ).apply {

            moduleFragment.accept(OriginalFunctionTransform(this), null)
            moduleFragment.accept(EntryPointTransform(this), null)

            debug("ARM CLASSES") { "\n\n" + armClasses.joinToString("\n\n") { it.dump() } }
            debug("ARM ENTRY POINTS") { "\n\n" + armEntryPoints.joinToString("\n\n") { it.dump() } }

            armClasses
                .map {
                    ArmClassBuilder(this, it).apply { buildIrClassWithoutGenBodies() }
                }
                .forEach {
                    it.buildGenFunctionBodies()
                    it.armClass.originalFunction.file.addChild(it.irClass)
                }

            armEntryPoints
                .forEach { ArmEntryPointBuilder(this, it).entryPointBody() }

            // --------  finishing up  --------
            debug("KOTLIN LIKE") { "\n\n" + moduleFragment.dumpKotlinLike() }
            // debug("DUMP AFTER") { "\n\n" + moduleFragment.dump() }
        }
    }

}

