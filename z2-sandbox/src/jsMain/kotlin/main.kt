import hu.simplexion.z2.adaptive.adaptive
import hu.simplexion.z2.adaptive.dom.AdaptiveDOMAdapter
import hu.simplexion.z2.adaptive.dom.html.Button
import hu.simplexion.z2.adaptive.dom.html.Text
import kotlinx.browser.window

fun main() {
    adaptive(AdaptiveDOMAdapter(window.document.body!!)) {
        var counter = 0
        Button("Click here!") { counter += 1 }
        Text("Counter: $counter")
    }
}