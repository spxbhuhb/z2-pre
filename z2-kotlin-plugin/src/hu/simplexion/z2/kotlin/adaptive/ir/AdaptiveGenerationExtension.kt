/*
 * Copyright © 2022-2023, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.z2.kotlin.adaptive.ir

import hu.simplexion.z2.kotlin.Z2Options
import hu.simplexion.z2.kotlin.adaptive.ir.ir2rum.EntryPointTransform
import hu.simplexion.z2.kotlin.adaptive.ir.ir2rum.OriginalFunctionTransform
import org.jetbrains.kotlin.backend.common.extensions.IrGenerationExtension
import org.jetbrains.kotlin.backend.common.extensions.IrPluginContext
import org.jetbrains.kotlin.ir.declarations.IrModuleFragment
import org.jetbrains.kotlin.ir.util.addChild
import org.jetbrains.kotlin.ir.util.dump
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

            // --------  IR to RUM  --------

            moduleFragment.accept(OriginalFunctionTransform(this), null)
            moduleFragment.accept(EntryPointTransform(this), null)

            if (compilationError) return // prevent the plugin to go on if there is an error that would result in an incorrect IR tree

            debug("RUM CLASSES") { "\n\n" + rumClasses.joinToString("\n\n") { it.dump() } }
            debug("RUM ENTRY POINTS") { "\n\n" + rumEntryPoints.joinToString("\n\n") { it.dump() } }

            // --------  RUM to AIR  --------

            rumClasses.forEach { airClasses[it.fqName] = it.toAir(this) }
            rumEntryPoints.forEach { airEntryPoints += it.toAir(this) }

            debug("AIR CLASSES") { "\n\n" + airClasses.values.joinToString("\n\n") { it.dump() } }
            debug("AIR ENTRY POINTS") { "\n\n" + airEntryPoints.joinToString("\n\n") { it.dump() } }

            // --------  AIR to IR  --------

            airClasses.values.forEach {
                it.toIr(this)
                if (! it.rumClass.compilationError) {
                    it.rumElement.originalFunction.file.addChild(it.irClass)
                }
            }
            airEntryPoints.forEach {
                if (! it.airClass.rumClass.compilationError) {
                    it.toIr(this)
                }
            }

            // --------  finishing up  --------

            debug("DUMP AFTER") { "\n\n" + moduleFragment.dump() }
            debug("KOTLIN LIKE") { "\n\n" + moduleFragment.dumpKotlinLike() }

        }
    }

}

