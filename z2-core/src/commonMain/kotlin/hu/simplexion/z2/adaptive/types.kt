package hu.simplexion.z2.adaptive

typealias AdaptiveExternalPatchType<BT> = (it: AdaptiveFragment<BT>) -> Unit

typealias AdaptiveFragmentFactory<BT> = (parent: AdaptiveFragment<BT>, index : Int) -> AdaptiveFragment<BT>?