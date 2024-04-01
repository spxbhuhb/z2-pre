package hu.simplexion.z2.browser.immaterial.button

import hu.simplexion.z2.browser.css.*
import hu.simplexion.z2.browser.html.Z2
import hu.simplexion.z2.localization.text.LocalizedText

val filledButtonStyles = arrayOf(
    displayFlex,
    flexDirectionRow,
    alignItemsCenter,
    primary,
    onPrimaryText,
    pr8,
    pl20,
    h40,
    cursorPointer,
    borderRadius20,
    whiteSpaceNoWrap,
    wMinContent,
    labelLarge,
    minWidthAuto
)

val textButtonStyles = arrayOf(
    positionRelative,
    displayFlex,
    flexDirectionRow,
    alignItemsCenter,
    primaryText,
    pr12,
    pl12,
    h40,
    cursorPointer,
    whiteSpaceNoWrap,
    wMinContent,
    labelLarge,
    borderRadius20,
    minWidthAuto
)

fun <T> Z2.filledSelectButton(options: List<T>, value: T, label: LocalizedText? = null, onSelectFun: (T) -> Unit) =
    SelectButton(
        this,
        filledButtonStyles,
        SelectButtonState(
            options,
            value,
            label,
            onSelectFun
        )
    )

fun <T> Z2.textSelectButton(options: List<T>, value: T, label: LocalizedText? = null, onSelectFun: (T) -> Unit) =
    SelectButton(
        this,
        textButtonStyles,
        SelectButtonState(
            options,
            value,
            label,
            onSelectFun
        )
    )