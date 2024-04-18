package hu.simplexion.z2.schematic.entity

import hu.simplexion.z2.schematic.Schematic
import hu.simplexion.z2.util.UUID

abstract class SchematicEntity<T : SchematicEntity<T>> : Schematic<T>(){
    abstract var uuid : UUID<T>
    abstract var name : String
}