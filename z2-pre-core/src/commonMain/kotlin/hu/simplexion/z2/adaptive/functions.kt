package hu.simplexion.z2.adaptive

fun replacedByPlugin(message: String): Nothing {
    throw IllegalStateException(message)
}

fun adapter() : AdaptiveAdapter<*> {
    replacedByPlugin("gets the adapter of the fragment")
}

fun fragment() : AdaptiveFragment<*> {
    replacedByPlugin("gets the fragment")
}

fun <T : AdaptiveTransformInterface> thisState() : T {
    replacedByPlugin("gets the fragment as a transfrom interface")
}