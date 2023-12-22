/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.z2.rui.testing

import hu.simplexion.z2.rui.RuiAdapter
import hu.simplexion.z2.rui.RuiAdapterFactory

object RuiTestAdapterFactory : RuiAdapterFactory() {

    override fun accept(vararg args: Any?): RuiAdapter<TestNode> {
        return RuiTestAdapter()
    }

}