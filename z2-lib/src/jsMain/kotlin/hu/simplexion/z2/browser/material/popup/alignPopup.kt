/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
@file:Suppress("DuplicatedCode")

package hu.simplexion.z2.browser.material.popup

import hu.simplexion.z2.browser.html.Z2
import hu.simplexion.z2.browser.material.px
import kotlinx.browser.window
import org.w3c.dom.DOMRect

/**
 * Positions [popup] relative to [anchor] according to the space
 * available and the desired height of the [popup].
 */
fun alignPopup(popup: Z2, anchor: Z2, minHeight: Double, minWidth: Double) {
    val anchorRect = anchor.htmlElement.getBoundingClientRect()
    val popupRect = popup.htmlElement.getBoundingClientRect()

    alignPopupVertically(anchorRect, popupRect, popup, minHeight)
    alignPopupHorizontally(anchorRect, popupRect, popup, minWidth)
}

fun alignPopupVertically(anchorRect: DOMRect, popupRect: DOMRect, popupElement: Z2, minHeight: Double) {
    val spaceBelow = window.innerHeight - (anchorRect.top + anchorRect.height)
    val spaceAbove = anchorRect.top
    val maxHeight = popupRect.height

//    var current : HTMLElement? = anchorElement
//    while (current != null) {
//        console.log("current", current)
//        console.log("current.getBoundingClientRect", current.getBoundingClientRect())
//        console.log("current.offsetTop", current.offsetTop)
//        console.log("current.offsetParent", current.offsetParent)
//        console.log("========")
//        current = current.offsetParent as? HTMLElement
//    }
//
//    console.log("popupElement", popupElement, popupRect)
//    console.log("minHeight", minHeight)
//    console.log("maxHeight", maxHeight)
//    console.log("spaceBelow", spaceBelow)
//    console.log("spaceAbove", spaceAbove)

    when {
        spaceBelow > minHeight -> {
            if (spaceBelow > maxHeight) {
                popupElement.style.height = maxHeight.px
                popupElement.style.top = (anchorRect.top + anchorRect.height).px  // FIXME correct popup positioning, hacked for now, "more" uses it
            } else {
                popupElement.style.maxHeight = spaceBelow.px
                popupElement.style.top = anchorRect.top.px
            }
        }

        spaceAbove > minHeight -> {
            if (spaceAbove > maxHeight) {
                popupElement.style.height = maxHeight.px
                popupElement.style.top = (anchorRect.top - maxHeight).px
            } else {
                popupElement.style.height = spaceAbove.px
                popupElement.style.top = 0.px
            }
        }
        // TODO try to position the element in the middle.

        // fallback, there is not enough space, just put it there and shrink it
        else -> {
            popupElement.style.height = spaceBelow.px
            popupElement.style.top = (-spaceAbove).px
        }
    }
}

fun alignPopupHorizontally(anchorRect: DOMRect, popupRect: DOMRect, popupElement: Z2, minWidth: Double) {
    val spaceAfter = window.innerWidth - (anchorRect.left + anchorRect.width)
    val spaceBefore = anchorRect.left
    val maxWidth = popupRect.width

    when {
        spaceAfter > minWidth -> {
            if (spaceAfter > maxWidth) {
                popupElement.style.width = maxWidth.px
                popupElement.style.left = anchorRect.left.px
            } else {
                popupElement.style.maxWidth = spaceAfter.px
                popupElement.style.left = anchorRect.left.px
            }
        }

        spaceBefore > minWidth -> {
            if (spaceBefore > maxWidth) {
                popupElement.style.width = maxWidth.px
                popupElement.style.left = (anchorRect.left + anchorRect.width - maxWidth).px // FIXME correct popup positioning, hacked for now, "more" uses it
            } else {
                popupElement.style.width = spaceBefore.px
                popupElement.style.left = 0.px
            }
        }
        // TODO try to position the element in the middle.

        // fallback, there is not enough space, just put it there and shrink it
        else -> {
            popupElement.style.width = spaceAfter.px
            popupElement.style.left = (-spaceBefore).px
        }
    }
}