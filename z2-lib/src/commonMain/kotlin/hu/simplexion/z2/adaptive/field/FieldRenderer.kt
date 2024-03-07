package hu.simplexion.z2.adaptive.field

interface FieldRenderer<FT : AdaptiveField<VT>,VT> {

    val field: FT

    val fieldValue: FieldValue<VT>
        get() = this.field.fieldValue

    val fieldState: FieldState
        get() = this.field.fieldState

    val fieldConfig: FieldConfig
        get() = this.field.fieldConfig

    val hasFocus
        get() = fieldState.hasFocus

    val error
        get() = fieldState.touched && (fieldState.error || fieldState.invalidInput)

    val readOnly
        get() = fieldConfig.readOnly

}