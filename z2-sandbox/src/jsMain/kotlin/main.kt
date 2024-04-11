import hu.simplexion.z2.adaptive.adaptive
import hu.simplexion.z2.adaptive.dom.AdaptiveDOMAdapter
import hu.simplexion.z2.adaptive.dom.html.Button
import hu.simplexion.z2.adaptive.dom.html.Text
import hu.simplexion.z2.adaptive.worker.poll
import kotlinx.browser.window
import kotlinx.datetime.Clock
import kotlin.time.Duration.Companion.seconds

fun main() {
    adaptive(AdaptiveDOMAdapter(window.document.body!!)) {
        var counter = 0
        val time = poll(1.seconds, default = Clock.System.now()) { Clock.System.now() }
        Button("Click here!") { counter += 1 }
        Text("Counter: $counter Time:$time")
    }
}