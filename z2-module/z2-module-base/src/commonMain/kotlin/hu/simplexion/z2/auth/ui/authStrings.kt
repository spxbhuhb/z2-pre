package hu.simplexion.z2.auth.ui

import hu.simplexion.z2.commons.localization.text.ICommonStrings

object authStrings : IAuthStrings

interface IAuthStrings : ICommonStrings {
    val login get() = static("Belépés")

    val role get() = static("Szerepkör")
    val roles get() = static("Szerepkörök")
    val rolesSupport get() = support(roles, "Szerepkörök listája, hozzáadás, szerepkörrel rendelkező felhasználók.")
    val addRole get() = static("Szerepkör hozzáadása")
    val editRole get() = static("Szerepkör szerkesztése")
    val removeRole get() = static("Szerepkör törlése")

    val account get() = static("Fiók")
    val accounts get() = static("Fiókok")
    val accountsSupport get() = support(accounts, "Felhasználói fiókok kezelése. Hozzáadás, szerkesztés, jelszó beállítás.")
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

    val securityPolicy get() = static("Biztonsági házirend")
    val securityPolicySupport get() = support(securityPolicy, "Biztonsági szabályok beállítása. Jelszóerősség, hibás bejelentkezések száma stb.")

}
