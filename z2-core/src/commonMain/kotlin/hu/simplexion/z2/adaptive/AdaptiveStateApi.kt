package hu.simplexion.z2.adaptive

interface AdaptiveStateApi {
    val fragment : AdaptiveFragment<*>
    val state : Array<Any?>
}