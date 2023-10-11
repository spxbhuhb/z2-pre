package hu.simplexion.z2.schematic

class SchematicChange(
    val path: String,
    val value: Any?
) {
    fun patch(values : MutableMap<String, Any?>) {
        values[path] = value
    }
}