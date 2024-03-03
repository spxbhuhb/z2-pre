package hu.simplexion.z2.adaptive.field

import hu.simplexion.z2.localization.icon.LocalizedIcon
import hu.simplexion.z2.schematic.Schematic

class FieldConfig : Schematic<FieldConfig>() {

    /**
     * The textual label of the field or null if no label should be displayed.
     */
    var label by string().nullable()

    /**
     * When true [supportText] is displayed to help the user.
     */
    var supportEnabled by boolean()

    /**
     * Text to help the user fill the field. A date format or a hint.
     */
    val supportText by string()

    var readOnly by boolean()

    var leadingIcon by generic<LocalizedIcon?>()

    var trailingIcon by generic<LocalizedIcon?>()

    var errorIcon by generic<LocalizedIcon?>()
}