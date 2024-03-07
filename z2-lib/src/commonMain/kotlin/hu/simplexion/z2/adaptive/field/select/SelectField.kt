package hu.simplexion.z2.adaptive.field.select

import hu.simplexion.z2.adaptive.field.AdaptiveField
import hu.simplexion.z2.adaptive.field.FieldConfig
import hu.simplexion.z2.adaptive.field.FieldState
import hu.simplexion.z2.adaptive.field.FieldValue
import hu.simplexion.z2.util.localLaunch

/**
 * A field that lets the user select a value from options.
 *
 * @param  VT  The type of the selected value.
 * @param  OT  Type of the options.
 */
class SelectField<VT, OT>(
    override val fieldValue: FieldValue<VT>,
    override val fieldState: FieldState,
    override val fieldConfig: FieldConfig,
    val selectState: SelectState<OT>,
    val selectConfig: SelectConfig<VT, OT>
) : AdaptiveField<VT> {

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