package hu.simplexion.z2.schematic.kotlin

import hu.simplexion.z2.schematic.kotlin.fir.SchematicDeclarationGenerator
import org.jetbrains.kotlin.fir.extensions.FirExtensionRegistrar


class SchematicPluginRegistrar : FirExtensionRegistrar() {
    override fun ExtensionRegistrarContext.configurePlugin() {
        +::SchematicDeclarationGenerator
    }
}