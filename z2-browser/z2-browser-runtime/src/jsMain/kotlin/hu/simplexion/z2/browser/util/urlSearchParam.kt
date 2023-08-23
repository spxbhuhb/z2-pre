package hu.simplexion.z2.browser.util

import org.w3c.dom.url.URLSearchParams

@Suppress("UNCHECKED_CAST_TO_EXTERNAL_INTERFACE")
fun URLSearchParams.keys() : Iterable<String> {
    val keys = this.asDynamic().keys() ?: error("not keys() on $this")
    val jsIterator = keys as JsIterator<String>
    return jsIterator.iterable()
}