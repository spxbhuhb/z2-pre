package hu.simplexion.z2.browser.nonmaterial.schematic

import hu.simplexion.z2.browser.html.Z2
import hu.simplexion.z2.browser.material.button.textButton
import hu.simplexion.z2.commons.i18n.LocalizedText
import hu.simplexion.z2.schematic.runtime.Schematic
import org.w3c.dom.events.Event

/**
 * A text button that is disabled when [schematic] is invalid.
 */
fun Z2.schematicTextButton(schematic: Schematic<*>, title : LocalizedText, onClick : (event : Event) -> Unit) =
    textButton(title, onClick).also { button ->
        button.isDisabled = schematic.isValid
    }