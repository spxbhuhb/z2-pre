package hu.simplexion.z2.adaptive.field

import hu.simplexion.z2.schematic.Schematic

class FieldState : Schematic<FieldState>() {

    /**
     * When true, the field is waiting for some data to arrive. This should be
     * shown on the screen to let the user know that there is something the fields
     * is waiting for,
     */
    var loading by boolean()

    /**
     * True when the field has the focus.
     */
    var hasFocus by boolean()

    /**
     * True when the field has been touched by the user or the user
     * tried to submit the form without touching the field.
     *
     * Fields hide errors when [touched] is false. This gives better
     * user experience as the forms don't start with most field in
     * error.
     */
    var touched by boolean()

    /**
     * True when the user entered an input which cannot be parsed by
     * the field. For example a wrongly formatted date.
     *
     * Forms should not be submitted when there are invalid inputs.
     *
     * There is an important difference between [error] and [invalidInput].
     * As the invalid input cannot be parsed, the actual data backing the field
     * won't be changed. This may result in a successful validation. However,
     * the application should not let the user submit the form as the value
     * displayed would not be the same as the one submitted.
     */
    var invalidInput by boolean()

    /**
     * True when there is a validation error on the field. Typically, changes
     * in the backing data trigger a full validation of the schematic instance
     * that backs the form. When the validation for a field fails [error] is
     * set to `true` when it succeeds [error] is set to `false`.
     */
    var error by boolean()

    /**
     * The error text to display to the user. This is the reason of the validation
     * fail in most cases. Displayed only when [error] is true.
     */
    var errorText by string().nullable()

}