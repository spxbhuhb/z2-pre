package hu.simplexion.z2.schematic

import hu.simplexion.z2.schematic.schema.Schema
import hu.simplexion.z2.serialization.InstanceDecoder
import hu.simplexion.z2.serialization.InstanceEncoder
import hu.simplexion.z2.serialization.Message
import hu.simplexion.z2.serialization.MessageBuilder
import hu.simplexion.z2.util.placeholder

interface SchematicCompanion<T : Schematic<T>> : InstanceEncoder<T>, InstanceDecoder<T> {

    /**
     * The fully qualified name of the schematic class this companion belongs to.
     * This field is independent of any reflection frameworks, a getter that returns
     * with the FQN is added during compilation time.
     */
    val schematicFqName : String
        get() = placeholder()

    val schematicSchema : Schema<T>
        get() = placeholder()

    fun newInstance() : T = placeholder()

    override fun encodeInstance(builder: MessageBuilder, value: T) = placeholder()

    override fun decodeInstance(message: Message?) = placeholder()

    operator fun invoke(builder : T.() -> Unit) : T =
        newInstance().apply(builder)

    fun setFieldValue(instance : T, name : String, value : Any?) : Unit = placeholder()

    fun getFieldValue(instance : T, name : String) : Any? = placeholder()

}