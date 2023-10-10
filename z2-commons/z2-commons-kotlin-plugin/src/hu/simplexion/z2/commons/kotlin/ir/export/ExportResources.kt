package hu.simplexion.z2.commons.kotlin.ir.export

import hu.simplexion.z2.commons.kotlin.ir.CommonsPluginContext
import org.jetbrains.kotlin.ir.backend.js.transformers.irToJs.safeName
import org.jetbrains.kotlin.ir.declarations.IrModuleFragment
import java.nio.file.Files
import java.nio.file.StandardOpenOption

class ExportResources(
    val moduleFragment: IrModuleFragment,
    val pluginContext: CommonsPluginContext
) {
    fun export() {
        val path = pluginContext.options.resourceOutputDir.toPath().resolve("localization." + moduleFragment.safeName + ".txt")
        Files.write(path, pluginContext.resources.joinToString("\n").encodeToByteArray(), StandardOpenOption.TRUNCATE_EXISTING)
    }
}