package hu.simplexion.z2.localization.text

import hu.simplexion.z2.localization.localizedTextStore

class StaticText(
    override val key: String,
    override var value: String,
) : LocalizedText {

    // TODO cleanup StaticText, there are instances in the interface getters

    override fun toString(): String {
        return localizedTextStore[key]?.value ?: value
    }

    val localized
        get() = toString() // TODO think about storing the value in the store only but not in the object

    fun localizedReplace(vararg args : Pair<String, Any>) : String {
        var result = toString()
        for(arg in args) {
            result = result.replace("{${arg.first}}", arg.second.toString())
        }
        return result
    }

    fun localizedReplace(vararg args : Any) : String {
        val result = toString()
        val parts = result.split("%N")
        if (parts.size == 1) return result

        return parts.zip(args).joinToString("") { "${it.first}${it.second}" } + parts.drop(args.size).joinToString("")
    }
}