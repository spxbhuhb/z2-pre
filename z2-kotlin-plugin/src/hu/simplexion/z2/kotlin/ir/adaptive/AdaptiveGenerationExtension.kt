/*
 * Copyright © 2022-2023, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.z2.kotlin.ir.adaptive

import hu.simplexion.z2.kotlin.Z2Options
import hu.simplexion.z2.kotlin.ir.adaptive.ir2rum.EntryPointTransform
import hu.simplexion.z2.kotlin.ir.adaptive.ir2rum.OriginalFunctionTransform
import hu.simplexion.z2.kotlin.ir.adaptive.plugin.AdaptiveDumpPoint
import hu.simplexion.z2.kotlin.ir.adaptive.plugin.AdaptiveOptions
import hu.simplexion.z2.kotlin.ir.adaptive.plugin.AdaptiveRootNameStrategy
import org.jetbrains.kotlin.backend.common.extensions.IrGenerationExtension
import org.jetbrains.kotlin.backend.common.extensions.IrPluginContext
import org.jetbrains.kotlin.ir.declarations.IrModuleFragment
import org.jetbrains.kotlin.ir.util.*

internal class AdaptiveGenerationExtension(
    val options: Z2Options
) : IrGenerationExtension {

    override fun generate(moduleFragment: IrModuleFragment, pluginContext: IrPluginContext) {

        val options = AdaptiveOptions(
            annotations = listOf(ADAPTIVE_ANNOTATION),
            dumpPoints = listOf(AdaptiveDumpPoint.AdaptiveTree, AdaptiveDumpPoint.AirTree, AdaptiveDumpPoint.After, AdaptiveDumpPoint.KotlinLike),
            rootNameStrategy = AdaptiveRootNameStrategy.StartOffset,
            withTrace = false,
            exportState = false,
            importState = false,
            printDumps = true,
            pluginLogDir = null
        )

        AdaptivePluginContext(
            pluginContext,
            moduleFragment,
            options,
        ).apply {

            // --------  preparations  --------

            pluginLogDir?.let {
                report(
                    IrMessageLogger.Severity.WARNING,
                    "adaptive.pluginLogDir is set to: $it",
                    IrMessageLogger.Location(moduleFragment.name.asString(), 1, 1)
                )
            }

            AdaptiveDumpPoint.Before.dump(this) {
                output("DUMP BEFORE", moduleFragment.dump())
            }

            // --------  IR to RUM  --------

            moduleFragment.accept(OriginalFunctionTransform(this), null)
            moduleFragment.accept(EntryPointTransform(this), null)

            if (compilationError) return // prevent the plugin to go on if there is an error that would result in an incorrect IR tree

            AdaptiveDumpPoint.AdaptiveTree.dump(this) {
                output("RUM CLASSES", rumClasses.joinToString("\n\n") { it.dump() })
                output("RUM ENTRY POINTS", rumEntryPoints.joinToString("\n\n") { it.dump() })
            }

            // --------  RUM to AIR  --------

            rumClasses.forEach { airClasses[it.fqName] = it.toAir(this) }
            rumEntryPoints.forEach { airEntryPoints += it.toAir(this) }

            AdaptiveDumpPoint.AirTree.dump(this) {
                output("AIR CLASSES", airClasses.values.joinToString("\n\n") { it.dump() })
                output("AIR ENTRY POINTS", airEntryPoints.joinToString("\n\n") { it.dump() })
            }

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

            AdaptiveDumpPoint.After.dump(this) {
                output("DUMP AFTER", moduleFragment.dump())
            }

            AdaptiveDumpPoint.KotlinLike.dump(this) {
                output("KOTLIN LIKE", moduleFragment.dumpKotlinLike())
            }

        }
    }

}

