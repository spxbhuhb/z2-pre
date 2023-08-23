package hu.simplexion.z2.browser.routing

import hu.simplexion.z2.commons.i18n.LocalizedIcon
import hu.simplexion.z2.commons.i18n.LocalizedText

interface RoutingTarget<R> {

    var parent: Router<R>?

    var relativePath : String

    val label : LocalizedText?

    val icon : LocalizedIcon?

    fun accepts(path : List<String>) : Boolean {
        return path.first() == relativePath
    }

    fun open() {
        root.open(this)
    }

    fun open(receiver: R, path: List<String>)

    fun open(target: RoutingTarget<R>) {
        root.open(target)
    }

    fun openWith(target: RoutingTarget<R>, vararg parameters : Any) {
        root.openWith(target, *parameters)
    }

    val absolutePath : List<String>
        get() {
            val path = mutableListOf(relativePath)
            var current = this
            while (current.parent != null) {
                current = current.parent!!
                path += current.relativePath
            }
            return path.reversed()
        }

    val root: RoutingTarget<R>
        get() {
            var current = this
            while (current.parent != null) {
                current = current.parent!!
            }
            return current
        }

}