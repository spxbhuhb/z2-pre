package hu.simplexion.z2.browser.field.stereotype

import hu.simplexion.z2.browser.field.FieldState
import hu.simplexion.z2.browser.html.Z2
import hu.simplexion.z2.browser.material.textfield.FieldConfig
import hu.simplexion.z2.localization.text.LocalizedText

fun Z2.decimalField(value : Long, scale : Int, label : LocalizedText, changeFun : (Long) -> Unit) =
    DecimalField(
        this,
        FieldState(label),
        FieldConfig(
            decodeFromString = { throw IllegalStateException() }
        ) { changeFun(it.value) },
        scale
    ).also {
        it.main()
        it.value = value
    }

fun Z2.longField(value : Long, label : LocalizedText, changeFun : (Long) -> Unit) =
    LongField(
        this,
        FieldState(label),
        FieldConfig(
            decodeFromString = { it.toLongOrNull() ?: 0L }
        ) { changeFun(it.value) }
    ).also {
        it.main()
        it.value = value
    }