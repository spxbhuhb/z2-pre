package hu.simplexion.z2.adaptive.field.select

fun interface SelectGet<VT,OT> {

    suspend fun get(field : SelectField<VT, OT>, value : VT) : OT

}