package hu.simplexion.z2.browser

import hu.simplexion.z2.localization.text.LocalizedTextProvider

object browserStrings : IBrowserStrings

interface IBrowserStrings : LocalizedTextProvider {
    val actions get() = static("műveletek")
    val add get() = static("hozzáadás")
    val back get() = static("vissza")
    val cancel get() = static("mégse")
    val discardChanges get() = static("Változások eldobása?")
    val discardChangesMessage get() = static("A mentéshez klikk a Mégse gombra és utána mentés.")
    val edit get() = static("szerkesztés")
    val export get() = static("export")
    val name get() = static("név")
    val no get() = static("nem")
    val open get() = static("megnyitás")
    val searchHint get() = static("keresés")
    val settings get() = static("beállítások")
    val yes get() = static("igen")
    val _empty get() = static("")
    val createdBy get() = static("létrehozó")
    val createdAt get() = static("létrehozás")
    val content get() = static("tartalom")
    val details get() = static("részletek")
    val ok get() = static("rendben")
    val previous get() = static("előző")
    val next get() = static("következő")
    val searchInProgress get() = static("...keresés...")
    val noHits get() = static("nincs találat")
    val dropFileHere get() = static("dobja ide a fájlt vagy klikkeljen a kiválasztáshoz")
    val dropAttachmentHere get() = static("dobja ide a mellékleteket vagy klikkeljen a kiválasztáshoz")
    val fileAlreadyAdded get() = static("Ilyen nevű fájl már hozzá van adva.")
    val selectMainFirst get() = static("A mellékletek kiválasztása előtt a fájl ki kell választani.")
    val selectFolderFirst get() = static("Válasszon mappát!")
    val zeroBytes get() = static("0 bájt")
    val bytes get() = static("bájt")
    val noFilesSelected get() = static("nincs fájl kiválasztva")
    val filesSelected get() = static("fájl")
    val type get() = static("Típus")
    val bundleNameSupport get() = static("a dokumentum címe")
    val title get() = static("Cím")
    val file get() = static("Fájl")
    val attachments get() = static("Mellékletek")
    val folder get() = static("Mappa")
    val sizeOverLimit get() = static("mérethatár felett")
    val invalidExtension get() = static("nem elfogadott formátum")
}