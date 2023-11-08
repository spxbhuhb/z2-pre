package hu.simplexion.z2.kotlin

import hu.simplexion.z2.kotlin.fir.schematic.SchematicDeclarationGenerator
import org.jetbrains.kotlin.fir.extensions.FirExtensionRegistrar


class Z2PluginRegistrar : FirExtensionRegistrar() {
    override fun ExtensionRegistrarContext.configurePlugin() {
        +::SchematicDeclarationGenerator
    }
}