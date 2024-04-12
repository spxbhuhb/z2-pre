import hu.simplexion.z2.adaptive.adaptive
import hu.simplexion.z2.adaptive.css.displayFlex
import hu.simplexion.z2.adaptive.css.flexDirectionColumn
import hu.simplexion.z2.adaptive.css.p24
import hu.simplexion.z2.adaptive.dom.AdaptiveDOMAdapter
import hu.simplexion.z2.adaptive.html.button
import hu.simplexion.z2.adaptive.html.div
import hu.simplexion.z2.adaptive.html.text
import hu.simplexion.z2.adaptive.worker.poll
import kotlinx.browser.window
import kotlinx.datetime.Clock
import kotlin.time.Duration.Companion.seconds

fun main() {
    adaptive(AdaptiveDOMAdapter(window.document.body !!).also { it.trace = true }) {
        var counter = 0
        val time = poll(1.seconds, default = Clock.System.now()) { Clock.System.now() }
        div(displayFlex, flexDirectionColumn, p24) {
            button("Click here!") { counter += 1 }
            text("Counter: $counter Time:$time")
        }
    }
}