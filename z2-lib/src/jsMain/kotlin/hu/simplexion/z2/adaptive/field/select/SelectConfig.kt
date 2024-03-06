package hu.simplexion.z2.adaptive.field.select

import hu.simplexion.z2.adaptive.field.FieldRenderer
import hu.simplexion.z2.adaptive.field.text.TextField
import hu.simplexion.z2.browser.html.Z2
import hu.simplexion.z2.schematic.Schematic

class SelectConfig<VT,OT> : Schematic<SelectConfig<VT, OT>>() {

    /**
     * When true [query] should be used to get options.
     */
    var remote by boolean()

    /**
     * When `true`, the select will execute the query during the first render. This means
     * that values are fetched as soon as possible. This may improve user experience, but
     * it may increase server load.
     */
    var eager by boolean()

    /**
     * When [remote] and the user types into the field, at lease [minimumFilterLength]
     * characters should be typed in before the [query] is executed.
     */
    var minimumFilterLength by int()

    /**
     * The query to be executed to get options to show to the user.
     */
    var query by generic<SelectQuery<VT, OT>>()

    var get by generic<SelectGet<VT, OT>>()

    var renderer by generic<FieldRenderer<SelectField<VT, OT>,VT>>()

    var optionToValue by generic<(field : SelectField<VT,OT>, option : OT) -> VT>()

    var valueToString by generic<(field : SelectField<VT,OT>, value : VT) -> String>()

    var renderItem by generic<Z2.(field : SelectField<VT,OT>, option : OT) -> Unit>()

    var textFieldRenderer by generic<FieldRenderer<TextField,String>?>()

}