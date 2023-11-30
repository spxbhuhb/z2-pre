package hu.simplexion.z2.localization.text

lateinit var commonStrings : ICommonStrings

interface ICommonStrings : LocalizedTextProvider {

    val start get() = static("start", name = "start")
    val name get() = static("név", name = "name")
    val uuid get() = static("uuid", name = "uuid")
    val status get() = static("állapot", name = "status")
    val data get() = static("adat", name = "data")
    val add get() = static("hozzáadás", name = "add")
    val remove get() = static("eltávolítás", name = "remove")
    val update get() = static("változtatás", name = "update")
    val enabled get() = static("engedélyezve", name = "enabled")
    val path get() = static("útvonal", name = "path")
    val save get() = static("mentés", name = "save")
    val cancel get() = static("mégse", name = "cancel")

    val communicationError get() = static("Kommunikációs hiba, kérem frissítse az oldalt.")
    val timeoutError get() = static("A szerver nem válaszolt időben, kérem frissítse az oldalt.")
    val responseError get() = static("A szerver végrehajtási hibát jelzett, kérem vegye fel a kapcsolatot az üzemeltetéssel.")

}