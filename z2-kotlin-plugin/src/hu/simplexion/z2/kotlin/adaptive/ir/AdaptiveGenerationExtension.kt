/*
 * Copyright © 2022-2023, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.z2.kotlin.adaptive.ir

import hu.simplexion.z2.kotlin.Z2Options
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

            // --------  preparations  --------

//            pluginLogDir?.let {
//                report(
//                    IrMessageLogger.Severity.WARNING,
//                    "adaptive.pluginLogDir is set to: $it",
//                    IrMessageLogger.Location(moduleFragment.name.asString(), 1, 1)
//                )
//            }

            // debug("DUMP BEFORE") { "\n\n" + moduleFragment.dump() }

            // --------  IR to ARM  --------

            moduleFragment.accept(OriginalFunctionTransform(this), null)
            moduleFragment.accept(EntryPointTransform(this), null)

            if (compilationError) return // prevent the plugin to go on if there is an error that would result in an incorrect IR tree

            debug("ARM CLASSES") { "\n\n" + armClasses.joinToString("\n\n") { it.dump() } }
            debug("ARM ENTRY POINTS") { "\n\n" + armEntryPoints.joinToString("\n\n") { it.dump() } }

            // --------  ARM to AIR  --------

            armClasses.forEach { airClasses[it.fqName] = it.toAir(this) }
            armEntryPoints.forEach { airEntryPoints += it.toAir(this) }
            
            // --------  AIR to IR  --------

            airClasses.values.forEach {
                it.toIr(this)
                if (! it.armClass.compilationError) {
                    it.armClass.originalFunction.file.addChild(it.irClass)
                }
            }
            airEntryPoints.forEach {
                if (! it.airClass.armClass.compilationError) {
                    it.toIr(this)
                }
            }

            // --------  finishing up  --------
            debug("KOTLIN LIKE") { "\n\n" + moduleFragment.dumpKotlinLike() }
            // debug("DUMP AFTER") { "\n\n" + moduleFragment.dump() }
        }
    }

}

