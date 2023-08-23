package hu.simplexion.z2.schematic.runtime

typealias SchematicListener<ST> = (thisRef: ST, change: SchematicChange) -> Unit