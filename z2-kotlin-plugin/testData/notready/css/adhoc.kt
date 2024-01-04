package a.b.c

import hu.simplexion.z2.commons.browser.CssClass

val pt4 = CssClass("pt4-not-used")
val pt8 = CssClass("pt8-not-used")
val borderRadius8 = CssClass("not-used")

val String.css
    get() = CssClass(this)

fun div(vararg classes: CssClass): Array<out CssClass> {
    return classes
}

fun box(): String {

    var result = div(pt4)
    if (result.size != 1 && result.first().name != "pt-4") return "Fail: pt-4 vs. ${result.first().name}"

    result = div(pt4, pt8)
    if (result.size != 1 && result.first().name != "pt-4 pt-8") return "Fail: pt4 pt8 vs. ${result.first().name}"

    div(pt4, pt8).let {
        if (it.size != 1 && it.first().name != "pt-4 pt-8") return "Fail: pt4 pt8 vs. ${it.first().name}"
    }

    result = div(pt4, CssClass("hello"))
    if (result.size != 2) return "Fail: 2"
    if (result[0].name != "hello") return "Fail: 3 ${result[1].name}"
    if (result[1].name != "pt-4") return "Fail: 4 ${result[0].name}"

    val pt12 = CssClass("pt12-value")
    result = div(pt12, pt4, CssClass("hello"))
    if (result.size != 3) return "Fail: 23"
    if (result[0].name != "pt12-value") return "Fail: 24 ${result[0].name}"
    if (result[1].name != "hello") return "Fail: 25 ${result[1].name}"
    if (result[2].name != "pt-4") return "Fail: 26 ${result[2].name}"

    result = div(pt12, pt4, CssClass("hello"), pt8, borderRadius8)
    if (result.size != 3) return "Fail: 33"
    if (result[0].name != "pt12-value") return "Fail: 34 ${result[0].name}"
    if (result[1].name != "hello") return "Fail: 35 ${result[1].name}"
    if (result[2].name != "pt-4 pt-8 border-radius-8") return "Fail: 36 ${result[2].name}"

    result = div("hello".css)
    if (result.size != 1) return "Fail: 41"
    if (result[0].name != "hello") return "Fail: 42"

    return "OK"
}