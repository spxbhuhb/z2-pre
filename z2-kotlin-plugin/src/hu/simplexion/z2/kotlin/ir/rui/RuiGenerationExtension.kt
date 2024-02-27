/*
 * Copyright © 2022-2023, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.z2.kotlin.ir.rui

import hu.simplexion.z2.kotlin.Z2Options
import hu.simplexion.z2.kotlin.ir.rui.ir2rum.EntryPointTransform
import hu.simplexion.z2.kotlin.ir.rui.ir2rum.OriginalFunctionTransform
import hu.simplexion.z2.kotlin.ir.rui.plugin.RuiDumpPoint
import hu.simplexion.z2.kotlin.ir.rui.plugin.RuiOptions
import hu.simplexion.z2.kotlin.ir.rui.plugin.RuiRootNameStrategy
import org.jetbrains.kotlin.backend.common.extensions.IrGenerationExtension
import org.jetbrains.kotlin.backend.common.extensions.IrPluginContext
import org.jetbrains.kotlin.ir.declarations.IrModuleFragment
import org.jetbrains.kotlin.ir.util.*

internal class RuiGenerationExtension(
    val options: Z2Options
) : IrGenerationExtension {

    override fun generate(moduleFragment: IrModuleFragment, pluginContext: IrPluginContext) {

        val options = RuiOptions(
            annotations = listOf(RUI_ANNOTATION),
            dumpPoints = listOf(RuiDumpPoint.RuiTree, RuiDumpPoint.AirTree, RuiDumpPoint.After, RuiDumpPoint.KotlinLike),
            rootNameStrategy = RuiRootNameStrategy.StartOffset,
            withTrace = false,
            exportState = false,
            importState = false,
            printDumps = true,
            pluginLogDir = null
        )

        RuiPluginContext(
            pluginContext,
            moduleFragment,
            options,
        ).apply {

            // --------  preparations  --------

            pluginLogDir?.let {
                report(
                    IrMessageLogger.Severity.WARNING,
                    "rui.pluginLogDir is set to: $it",
                    IrMessageLogger.Location(moduleFragment.name.asString(), 1, 1)
                )
            }

            RuiDumpPoint.Before.dump(this) {
                output("DUMP BEFORE", moduleFragment.dump())
            }

            // --------  IR to RUM  --------

            moduleFragment.accept(OriginalFunctionTransform(this), null)
            moduleFragment.accept(EntryPointTransform(this), null)

            if (compilationError) return // prevent the plugin to go on if there is an error that would result in an incorrect IR tree

            RuiDumpPoint.RuiTree.dump(this) {
                output("RUM CLASSES", rumClasses.joinToString("\n\n") { it.dump() })
                output("RUM ENTRY POINTS", rumEntryPoints.joinToString("\n\n") { it.dump() })
            }

            // --------  RUM to AIR  --------

            rumClasses.forEach { airClasses[it.fqName] = it.toAir(this) }
            rumEntryPoints.forEach { airEntryPoints += it.toAir(this) }

            RuiDumpPoint.AirTree.dump(this) {
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

            RuiDumpPoint.After.dump(this) {
                output("DUMP AFTER", moduleFragment.dump())
            }

            RuiDumpPoint.KotlinLike.dump(this) {
                output("KOTLIN LIKE", moduleFragment.dumpKotlinLike())
            }

        }
    }

}

