package hu.simplexion.z2.adaptive

class AdaptiveFragmentFactory<BT>(
    val declaringFragment: AdaptiveFragment<BT>,
    val declarationIndex : Int
) {
    fun build(parent: AdaptiveFragment<BT>) : AdaptiveFragment<BT> {
        return declaringFragment.build(parent, declarationIndex)
    }
}