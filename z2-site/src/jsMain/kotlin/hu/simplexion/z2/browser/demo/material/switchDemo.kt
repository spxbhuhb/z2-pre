package hu.simplexion.z2.browser.demo.material

import hu.simplexion.z2.browser.html.*
import hu.simplexion.z2.browser.immaterial.schematic.switchField
import hu.simplexion.z2.browser.layout.surfaceContainerLow
import hu.simplexion.z2.browser.material.px
import hu.simplexion.z2.browser.material.switch.switch
import hu.simplexion.z2.schematic.runtime.Schematic

class SwitchSchematic : Schematic<SwitchSchematic>() {
    var booleanValue by boolean(true)
}

fun Z2.switchDemo() =
    surfaceContainerLow {
        grid {
            gridTemplateColumns = 400.px
            gridAutoRows = "min-content"
            gridGap = 16.px

            switch(true) { }
            switch(false) { }

            val schematic = SwitchSchematic()

            switchField { schematic.booleanValue }
        }
    }