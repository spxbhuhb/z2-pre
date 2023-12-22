/*
 * Copyright © 2022-2023, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.z2.kotlin.ir

import hu.simplexion.z2.kotlin.ir.css.CssModuleTransform
import hu.simplexion.z2.kotlin.ir.css.CssPluginContext
import hu.simplexion.z2.kotlin.ir.localization.LocalizationPluginContext
import hu.simplexion.z2.kotlin.ir.localization.ModuleTransform
import hu.simplexion.z2.kotlin.ir.localization.export.ExportResources
import hu.simplexion.z2.kotlin.ir.rui.RUI_ANNOTATION
import hu.simplexion.z2.kotlin.ir.rui.RuiPluginContext
import hu.simplexion.z2.kotlin.ir.rui.ir2rum.OriginalFunctionTransform
import hu.simplexion.z2.kotlin.ir.rui.plugin.RuiDumpPoint
import hu.simplexion.z2.kotlin.ir.rui.plugin.RuiOptions
import hu.simplexion.z2.kotlin.ir.rui.plugin.RuiRootNameStrategy
import hu.simplexion.z2.kotlin.ir.schematic.SchematicPluginContext
import hu.simplexion.z2.kotlin.ir.schematic.access.SchematicAccessTransform
import hu.simplexion.z2.kotlin.ir.schematic.klass.SchematicModuleTransform
import hu.simplexion.z2.kotlin.ir.service.ServiceModuleTransform
import hu.simplexion.z2.kotlin.ir.service.ServicePluginContext
import hu.simplexion.z2.kotlin.ir.service.consumer.GetConsumerTransform
import hu.simplexion.z2.kotlin.ir.service.proto.ProtoCompanionVisitor
import org.jetbrains.kotlin.backend.common.extensions.IrGenerationExtension
import org.jetbrains.kotlin.backend.common.extensions.IrPluginContext
import org.jetbrains.kotlin.ir.declarations.IrModuleFragment
import org.jetbrains.kotlin.ir.util.*

internal class Z2GenerationExtension(
    val options: Z2Options
) : IrGenerationExtension {

    override fun generate(moduleFragment: IrModuleFragment, pluginContext: IrPluginContext) {
        localization(moduleFragment, pluginContext)
        schematic(moduleFragment, pluginContext)
        service(moduleFragment, pluginContext)
        // css(moduleFragment, pluginContext)
        rui(moduleFragment, pluginContext)
    }

    fun localization(moduleFragment: IrModuleFragment, pluginContext: IrPluginContext) {
        LocalizationPluginContext(options, pluginContext).apply {
            moduleFragment.accept(ModuleTransform(this), null)
            ExportResources(moduleFragment, this).export()
        }
    }

    fun schematic(moduleFragment: IrModuleFragment, pluginContext: IrPluginContext) {
        SchematicPluginContext(pluginContext).apply {
            SchematicModuleTransform(this).also {
                // collects classes to transform and creates a SchematicClassTransform for each
                // does not call any functions in the SchematicClassTransform apart the constructor
                moduleFragment.accept(it, null)
                // calls SchematicClassTransform.transformFields for each class transform
                it.transformFields()
            }

            moduleFragment.accept(SchematicAccessTransform(this), null)
        }
    }

    fun service(moduleFragment: IrModuleFragment, pluginContext: IrPluginContext) {
        ServicePluginContext(pluginContext).apply {

//            debug("service") { "====  START  ==".padEnd(80, '=') }

            moduleFragment.accept(ProtoCompanionVisitor(this, protoCache), null)
            moduleFragment.accept(ServiceModuleTransform(this), null)
            moduleFragment.accept(GetConsumerTransform(this), null)

//            debug("service") { moduleFragment.dump() }
//            debug("service") { "====  END  ====".padEnd(80, '=') }
        }
    }

    fun css(moduleFragment: IrModuleFragment, pluginContext: IrPluginContext) {
        CssPluginContext(options, pluginContext).apply {
            moduleFragment.accept(CssModuleTransform(this), null)
        }
    }

    fun rui(moduleFragment: IrModuleFragment, pluginContext: IrPluginContext) {
        val options = RuiOptions(
            annotations = listOf(RUI_ANNOTATION),
            dumpPoints = emptyList(),
            rootNameStrategy = RuiRootNameStrategy.StartOffset,
            withTrace = false,
            exportState = false,
            importState = false,
            printDumps = false,
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

            val ir2Rum = OriginalFunctionTransform(this)

            moduleFragment.accept(ir2Rum, null)

            if (compilationError) return // prevent the plugin to go on if there is an error that would result in an incorrect IR tree

            RuiDumpPoint.RuiTree.dump(this) {
                output("RUI CLASSES", ir2Rum.rumClasses.joinToString("\n\n") { it.dump() })
            }

            // --------  RUM to AIR  --------

            ir2Rum.rumClasses.forEach { airClasses[it.fqName] = it.toAir(this) }
            val airEntryPoints = ir2Rum.rumEntryPoints.map { it.toAir(this) }

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

