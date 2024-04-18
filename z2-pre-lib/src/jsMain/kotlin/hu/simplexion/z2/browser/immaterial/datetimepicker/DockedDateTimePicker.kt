package hu.simplexion.z2.browser.immaterial.datetimepicker

import hu.simplexion.z2.browser.css.gridGap24
import hu.simplexion.z2.browser.field.FieldState
import hu.simplexion.z2.browser.field.ValueField
import hu.simplexion.z2.browser.html.Z2
import hu.simplexion.z2.browser.html.grid
import hu.simplexion.z2.browser.material.datepicker.DatePickerConfig
import hu.simplexion.z2.browser.material.datepicker.DockedDatePicker
import hu.simplexion.z2.browser.material.timepicker.DockedTimePicker
import hu.simplexion.z2.browser.material.timepicker.TimePickerConfig
import hu.simplexion.z2.util.hereAndNow
import kotlinx.datetime.LocalDateTime

class DockedDateTimePicker(
    parent: Z2? = null,
    override val state: FieldState = FieldState(),
    val config: DateTimePickerConfig = DateTimePickerConfig()
) : Z2(parent), ValueField<LocalDateTime> {

    lateinit var datePicker: DockedDatePicker
    lateinit var timePicker: DockedTimePicker

    override var value: LocalDateTime
        get() = checkNotNull(valueOrNull) { "DockedDateTimePicker.value.get" }
        set(value) {
            valueOrNull = value
        }

    override var valueOrNull: LocalDateTime? = hereAndNow()
        set(value) {
            field = value

            if (::datePicker.isInitialized) datePicker.valueOrNull = value?.date
            if (::timePicker.isInitialized) timePicker.valueOrNull = value?.time
        }

    override fun main(): DockedDateTimePicker {

        grid("min-content min-content", "max-content", gridGap24) {
            DockedDatePicker(
                this,
                FieldState().also {
                    it.label = state.label
                    it.supportText = config.dateSupportText.toString()
                },
                DatePickerConfig().also {
                    it.onChange = { date ->
                        with(this@DockedDateTimePicker) {
                            value = LocalDateTime(date, valueOrNull?.time ?: hereAndNow().time)
                            config.onChange?.invoke(this, value)
                        }
                    }
                }
            ).also {
                datePicker = it
                it.valueOrNull = valueOrNull?.date
            }.main()

            DockedTimePicker(
                this,
                FieldState().also {
                    it.supportText = config.timeSupportText.toString()
                },
                TimePickerConfig().also {
                    it.onChange = { time ->
                        with(this@DockedDateTimePicker) {
                            value = LocalDateTime(valueOrNull?.date ?: hereAndNow().date, time)
                            config.onChange?.invoke(this, value)
                        }
                    }
                }
            ).also {
                timePicker = it
                it.valueOrNull = valueOrNull?.time
            }.main()
        }

        return this
    }

}

