package hu.simplexion.z2.kotlin.ir.localization.export

import hu.simplexion.z2.kotlin.ir.localization.LocalizationPluginContext
import org.jetbrains.kotlin.ir.backend.js.transformers.irToJs.safeName
import org.jetbrains.kotlin.ir.declarations.IrModuleFragment
import java.nio.file.Files
import java.nio.file.StandardOpenOption

class ExportResources(
    val moduleFragment: IrModuleFragment,
    val pluginContext: LocalizationPluginContext
) {
    fun export() {
        val path = pluginContext.options.resourceOutputDir.toPath()
        Files.createDirectories(path)
        Files.write(
            path.resolve("localization." + moduleFragment.safeName + ".txt"),
            pluginContext.resources.joinToString("\n").encodeToByteArray(),
            StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING
        )
    }
}