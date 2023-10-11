package hu.simplexion.z2.localization.text

object commonStrings : ICommonStrings

interface ICommonStrings : LocalizedTextProvider {

    val start get() = static("start")
    val name get() = static("név")
    val uuid get() = static("uuid")
    val status get() = static("állapot")
    val data get() = static("adat")
    val add get() = static("hozzáadás")
    val remove get() = static("eltávolítás")
    val update get() = static("változtatás")
    val enabled get() = static("engedélyezve")
    val path get() = static("útvonal")
    val save get() = static("mentés")
    val cancel get() = static("mégse")

}