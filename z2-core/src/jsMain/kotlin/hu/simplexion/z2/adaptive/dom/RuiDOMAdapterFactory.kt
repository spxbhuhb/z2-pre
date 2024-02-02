/*
 * Copyright © 2020-2021, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.z2.adaptive.dom

import hu.simplexion.z2.adaptive.RuiAdapter
import hu.simplexion.z2.adaptive.RuiAdapterFactory
import org.w3c.dom.Node

object RuiDOMAdapterFactory : RuiAdapterFactory() {

    override fun accept(vararg args: Any?): RuiAdapter<*>? {
        if (args.isEmpty()) return RuiDOMAdapter()

        args[0].let {
            if (it != null && it is Node) return RuiDOMAdapter(it)
        }

        return null
    }

}