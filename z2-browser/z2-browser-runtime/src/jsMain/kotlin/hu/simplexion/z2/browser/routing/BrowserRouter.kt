/*
 * Copyright © 2020-2023, Simplexion, Hungary and contributors. Use of this source code is governed by the Apache 2.0 license.
 */
package hu.simplexion.z2.browser.routing

import hu.simplexion.z2.browser.html.Z2
import hu.simplexion.z2.browser.material.basicStrings
import hu.simplexion.z2.browser.material.modal.confirm
import hu.simplexion.z2.browser.util.decodeURIComponent
import hu.simplexion.z2.browser.util.io
import kotlinx.browser.window
import org.w3c.dom.events.Event
import org.w3c.dom.get
import org.w3c.dom.set

open class BrowserRouter(
    var homePath: String = "/",
) : Router<Z2>() {

    lateinit var receiver: Z2

    val lastShownKey = "z2-nav-last-shown"

    var pendingModificationsEnabled = false
    var pendingModifications = false

    fun start() {
        window.addEventListener("popstate", ::onPopState)

        val current = window.history.state?.toString() ?: ""
        if (current.isEmpty()) {
            window.history.replaceState(incrementNavCounter(), "")
        } else {
            window.sessionStorage[lastShownKey] = current
        }

        val path = decodeURIComponent(window.location.pathname)

        open(path, window.location.search, window.location.hash)
    }

    fun home() {
        open(homePath, "", "")
    }

    override fun open(target: RoutingTarget<Z2>) {
        open(target.absolutePath.joinToString("/"))
    }

    override fun openWith(target: RoutingTarget<Z2>, vararg parameters : Any) {
        open(target.absolutePath.joinToString("/") + "/" + parameters.joinToString("/"))
    }

    fun open(pathname: String, search: String = "", hash: String = "", changeState: Boolean = true) {
        io {
            if (stopNavigationOnPending()) return@io

            val newPath = pathname.ifEmpty { "/" }

            var url = if (hash.isEmpty()) newPath else "$newPath#$hash"
            url = if (search.isEmpty()) url else "$url?$search"

            if (changeState) {
                window.history.pushState(incrementNavCounter(), "", url)
                trace { "[routing]  pushState  url=$url" }
            }

            val path = newPath.split('/')


//            val parameters = mutableMapOf("#" to hash)
//
//            val searchParams = URLSearchParams(search)
//            for (key in searchParams.keys()) {
//                searchParams.get(key)?.let { parameters.put(key, it) }
//            }

            receiver.clear()

            if (path.isEmpty() || (path.size == 1 && path.first().isEmpty())) {
                default(receiver, path)
            } else {
                open(receiver, path)
            }
        }
    }

    override fun default(receiver: Z2, path: List<String>) {
        if (window.location.pathname != "/") {
            window.history.pushState(incrementNavCounter(), "", "/")
            trace { "[routing]  home" }
        }
    }

    /**
     * Called when:
     *  - the user clicks on the "Back" button
     *  - the user clicks on the "Forward" button
     *  - the [back] function is called
     *  - direct call to window.history.back
     *  - direct call to window.history.forward
     */
    @Suppress("UNUSED_PARAMETER")
    fun onPopState(event: Event) {
        io {
            if (stopNavigationOnPending()) return@io

            val current = (window.history.state as? Int) ?: 0
            window.sessionStorage[lastShownKey] = current.toString()

            val path = decodeURIComponent(window.location.pathname)

            trace { "[routing]  onPopState  $path" }

            open(path, window.location.search, window.location.hash, false)
        }
    }

    suspend fun stopNavigationOnPending(): Boolean {
        if (pendingModificationsEnabled && pendingModifications) {
            if (!confirm(basicStrings.discardChanges, basicStrings.discardChangesMessage)) {
                return true
            } else {
                pendingModifications = false
            }
        }
        return false
    }

    fun incrementNavCounter(): Int {
        val navCounter = (window.sessionStorage["zk-nav-counter"]?.toInt() ?: window.history.length) + 1
        window.sessionStorage["zk-nav-counter"] = navCounter.toString()
        window.sessionStorage["zk-nav-last-shown"] = navCounter.toString()
        return navCounter
    }

    fun replace(
        path: String = decodeURIComponent(window.location.pathname),
        query: String = window.location.search,
        hash: String = window.location.hash
    ) {
        var url = if (hash.isEmpty()) path else "$path#$hash"
        url = if (query.isEmpty()) url else "$url?$query"
        window.history.replaceState(window.history.state, "", url)
    }

    fun forward() = window.history.forward()

    fun back() = window.history.back()

}