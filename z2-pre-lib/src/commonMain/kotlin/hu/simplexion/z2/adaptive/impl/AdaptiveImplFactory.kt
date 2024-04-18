package hu.simplexion.z2.adaptive.impl

import hu.simplexion.z2.services.placeholder
import hu.simplexion.z2.util.UUID

abstract class AdaptiveImplFactory(
    val uuid: UUID<AdaptiveImplFactory>
) {

    open fun new(parent: AdaptiveImpl): AdaptiveImpl = placeholder()

}