package hu.simplexion.z2.kotlin.services

import hu.simplexion.z2.kotlin.Z2Options
import hu.simplexion.z2.kotlin.Z2PrePluginRegistrar
import hu.simplexion.z2.kotlin.adaptive.ir.AdaptiveGenerationExtension
import hu.simplexion.z2.kotlin.ir.Z2GenerationExtension
import hu.simplexion.z2.kotlin.schematic.ir.SchematicGenerationExtension
import hu.simplexion.z2.kotlin.services.ir.ServicesGenerationExtension
import org.jetbrains.kotlin.backend.common.extensions.IrGenerationExtension
import org.jetbrains.kotlin.compiler.plugin.CompilerPluginRegistrar
import org.jetbrains.kotlin.config.CompilerConfiguration
import org.jetbrains.kotlin.fir.extensions.FirExtensionRegistrarAdapter
import org.jetbrains.kotlin.test.model.TestModule
import org.jetbrains.kotlin.test.services.EnvironmentConfigurator
import org.jetbrains.kotlin.test.services.TestServices
import java.io.File

class ExtensionRegistrarConfigurator(testServices: TestServices) : EnvironmentConfigurator(testServices) {
    override fun CompilerPluginRegistrar.ExtensionStorage.registerCompilerExtensions(
        module: TestModule,
        configuration: CompilerConfiguration
    ) {
        val options = Z2Options(
            resourceOutputDir = File("testData/generated"),
            pluginDebug = true,
            pluginLogDir = null //File("testData/log")
        )

        FirExtensionRegistrarAdapter.registerExtension(Z2PrePluginRegistrar())

        IrGenerationExtension.registerExtension(SchematicGenerationExtension(options))
        IrGenerationExtension.registerExtension(ServicesGenerationExtension(options))
        IrGenerationExtension.registerExtension(AdaptiveGenerationExtension(options))
        IrGenerationExtension.registerExtension(Z2GenerationExtension(options))
    }
}