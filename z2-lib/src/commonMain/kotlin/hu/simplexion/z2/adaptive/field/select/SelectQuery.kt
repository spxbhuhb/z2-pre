package hu.simplexion.z2.adaptive.field.select

fun interface SelectQuery<VT,OT> {

    suspend fun query(field : SelectField<VT, OT>, filter : String) : List<OT>

}