package hu.simplexion.z2.adaptive

class AdaptiveFragmentFactory<BT>(
    val closure: AdaptiveClosure<BT>,
    val index : Int
) {

    fun build(parent : AdaptiveFragment<BT>) : AdaptiveFragment<BT> {
        return closure.declarationScope.adaptiveBuild(closure, parent, index)
    }
}