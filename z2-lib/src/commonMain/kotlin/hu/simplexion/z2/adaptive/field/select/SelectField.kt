package hu.simplexion.z2.adaptive.field.select

import hu.simplexion.z2.adaptive.field.AdaptiveField
import hu.simplexion.z2.adaptive.field.FieldConfig
import hu.simplexion.z2.adaptive.field.FieldState
import hu.simplexion.z2.adaptive.field.FieldValue
import hu.simplexion.z2.schematic.Schematic
import hu.simplexion.z2.util.localLaunch

/**
 * A field that lets the user select a value from options.
 *
 * @param  VT  The type of the selected value.
 * @param  OT  Type of the options.
 */
class SelectField<VT, OT> : Schematic<SelectField<VT, OT>>(), AdaptiveField<VT> {

    override val fieldValue by schematic<FieldValue<VT>>()
    override val fieldState by schematic<FieldState>()
    override val fieldConfig by schematic<FieldConfig>()

    val selectState by schematic<SelectState<OT>>()
    val selectConfig by schematic<SelectConfig<VT, OT>>()

    /**
     * Incremented by one each time the user changes the filter. The select uses this
     * number to check if a query belongs to the current filter or to a previous one.
     */
    var revision = 0

    fun runGet() {
        val value = fieldValue.valueOrNull ?: return
        localLaunch {
            selectState.options = listOf(selectConfig.get.get(this@SelectField, value))
        }
    }

    fun runQuery(value: String) {
        val inputRevision = ++ revision

        if (value.length < selectConfig.minimumFilterLength) {
            selectState.running = false
            selectState.options = emptyList()
            return
        }

        if (! selectState.running) {
            selectState.running = true
        }

        localLaunch {
            selectConfig.query.query(this@SelectField, value).let {
                if (inputRevision != revision) return@localLaunch
                selectState.options = it
                selectState.running = false
            }
        }
    }
}