package hu.simplexion.z2

import hu.simplexion.z2.browser.IBrowserStrings

lateinit var baseStrings : IBaseStrings

interface IBaseStrings : IBrowserStrings {
    val login get() = static("Belépés")

    val role get() = static("Szerepkör")
    val roles get() = static("Szerepkörök", "Szerepkörök listája, hozzáadás, szerepkörrel rendelkező felhasználók.")
    val addRole get() = static("Szerepkör hozzáadása")
    val editRole get() = static("Szerepkör szerkesztése")
    val removeRole get() = static("Szerepkör törlése")

    val account get() = static("Fiók")
    val accounts get() = static("Fiókok", "Felhasználói fiókok kezelése. Hozzáadás, szerkesztés, jelszó beállítás.")
    val addAccount get() = static("Fiók hozzáadása")
    val editAccount get() = static("Fiók szerkesztése")

    val changeCredentials get() = static("Jelszó változtatás")

    val authenticateSuccess get() = static("Sikeres azonosítás")
    val authenticateFail get() = static("Sikertelen azonosítás")

    val accountNotFound get() = static("Nem létező fiók")

    val logout get() = static("Kilépés")
    val grantRole get() = static("Szerepkör megadása")
    val revokeRole get() = static("Szerepkör visszavonása")

    val setLocked get() = static("Fiók zárolás beállítása")
    val setActivated get() = static("Fiók aktiválás beállítása")

    val securityPolicy get() = static("Biztonsági házirend", "Biztonsági szabályok beállítása. Jelszóerősség, hibás bejelentkezések száma stb.")

    val upload get() = static("feltöltés")
    val download get() = static("letöltés")

    val histories get() = static("Naplók", "Biztonsági, technikai, hiba és üzleti szintű naplók.")

    val overview get() = static("Áttekintés")
    val security get() = static("Biztonsági")
    val technical get() = static("Technikai")
    val error get() = static("Hiba")
    val business get() = static("Üzleti")

    val historyOverview get() = static("Napló áttekintés")
    val securityHistory get() = static("Biztonsági napló")
    val technicalHistory get() = static("Technikai napló")
    val errorHistory get() = static("Hibanapló")
    val businessHistory get() = static("Üzleti napló")

    val languages get() = static("Nyelvek", "Új nyelv hozzáadása. Címkék és ikonok fordítása.")
    val addLocale get() = static("Nyelv hozzáadása")
    val editLanguage get() = static("Nyelv szerkesztése")
    val isoCode get() = static("ISO kód", "A nyelv ISO 639 Apha-2 kódja.")
    val countryCode get() = static("Ország kód", "Az ország ISO 3166 Alpha-2 kódja")
    val nativeName get() = static("Natív név", "A nyelv neve az adott nyelven.")

    val removeWorker get() = static("Remove Worker")
    val workers get() = static("Workers")
    val worker get() = static("Worker")
    val addWorker get() = static("Add Worker")
    val editWorker get() = static("Edit Worker")
    val missingProvider get() = static("Cannot start worker, provider is missing")
    val unexpectedError get() = static("unexpected error")
    val statusChange get() = static("Worker Status Change")
    val startRuntime get() = static("starting worker runtime and all auto-start workers")
    val stopRuntime get() = static("stopping all workers and the worker runtime")
    val addProvider get() = static("add worker provider")
    val provider get() = static("provider")
    val setStoppedDuringStart get() = static("Worker status set to stopped because it cannot be running during startup. This may happen if the system had an uncontrolled shutdown.")

}
