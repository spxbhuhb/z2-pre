package hu.simplexion.z2.commons.kotlin.services

import hu.simplexion.z2.commons.kotlin.ir.plugin.CommonsGenerationExtension
import hu.simplexion.z2.commons.kotlin.ir.plugin.CommonsOptions
import org.jetbrains.kotlin.backend.common.extensions.IrGenerationExtension
import org.jetbrains.kotlin.compiler.plugin.CompilerPluginRegistrar
import org.jetbrains.kotlin.config.CompilerConfiguration
import org.jetbrains.kotlin.test.model.TestModule
import org.jetbrains.kotlin.test.services.EnvironmentConfigurator
import org.jetbrains.kotlin.test.services.TestServices
import java.io.File

class ExtensionRegistrarConfigurator(testServices: TestServices) : EnvironmentConfigurator(testServices) {
    override fun CompilerPluginRegistrar.ExtensionStorage.registerCompilerExtensions(
        module: TestModule,
        configuration: CompilerConfiguration
    ) {
        IrGenerationExtension.registerExtension(
            CommonsGenerationExtension(
                CommonsOptions(
                    File("testData/generated")
                )
            )
        )
    }
}