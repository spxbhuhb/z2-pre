package hu.simplexion.z2.adaptive

class AdaptiveSupportFunction<BT>(
    fragment: AdaptiveFragment<BT>,
    val index : Int
) {
    val closure = fragment.createClosure!!

    fun invoke(vararg arguments : Any?) : Any? {
        return closure.owner.invoke(this, arguments)
    }
}