package hu.simplexion.z2.meta

interface ValueBinding<VT> {

    var value: VT

    val metadata : PropertyMetadata

    val callback : ((binding : ValueBinding<*>) -> Unit)?

    fun replaces(other: ValueBinding<*>) : Boolean

}