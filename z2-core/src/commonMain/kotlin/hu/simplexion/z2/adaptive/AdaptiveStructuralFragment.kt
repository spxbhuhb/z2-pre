package hu.simplexion.z2.adaptive

interface AdaptiveStructuralFragment<BT> : AdaptiveFragment<BT> {

    override fun adaptiveBuild(closure: AdaptiveClosure<BT>, parent: AdaptiveFragment<BT>, index: Int): AdaptiveFragment<BT> {
        shouldNotRun()
    }

    override fun adaptivePatch(fragment: AdaptiveFragment<BT>, index: Int) {
        shouldNotRun()
    }

    override fun adaptiveSelect(fragment: AdaptiveFragment<BT>, index: Int): Int {
        shouldNotRun()
    }

}