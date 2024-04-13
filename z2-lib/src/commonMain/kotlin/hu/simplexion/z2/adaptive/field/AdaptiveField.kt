package hu.simplexion.z2.adaptive.field

interface AdaptiveField<VT> {

    val fieldValue: FieldValue<VT>

    val fieldState: FieldState

    val fieldConfig: FieldConfig

}