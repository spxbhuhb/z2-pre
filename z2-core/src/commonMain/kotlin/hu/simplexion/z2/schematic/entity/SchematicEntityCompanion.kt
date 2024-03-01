package hu.simplexion.z2.schematic.entity

import hu.simplexion.z2.schematic.SchematicCompanion
import hu.simplexion.z2.util.placeholder

interface SchematicEntityCompanion<T : SchematicEntity<T>> : SchematicCompanion<T> {

    var schematicStore: SchematicEntityStore<T>
        get() = placeholder()
        set(value) = placeholder()

}