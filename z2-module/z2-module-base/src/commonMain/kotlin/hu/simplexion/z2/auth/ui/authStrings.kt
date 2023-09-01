package hu.simplexion.z2.auth.ui

import hu.simplexion.z2.commons.i18n.LocalizedTextStore
import hu.simplexion.z2.commons.util.UUID

object authStrings : LocalizedTextStore(UUID("3638da2e-03ec-4f8b-88bf-e5e7a66cb063")) {
    val login by "Belépés"

    val role by "Szerepkör"
    val roles by "Szerepkörök"
    val rolesSupport by roles.support("Szerepkörök listája, hozzáadás, szerepkörrel rendelkező felhasználók.")
    val addRole by "Szerepkör hozzáadása"
    val editRole by "Szerepkör szerkesztése"
    val removeRole by "Szerepkör törlése"

    val account by "Fiók"
    val accounts by "Fiókok"
    val accountsSupport by accounts.support("Felhasználói fiókok kezelése. Hozzáadás, szerkesztés, jelszó beállítás.")
    val addAccount by "Fiók hozzáadása"
    val editAccount by "Fiók szerkesztése"

    val changeCredentials by "Jelszó változtatás"

    val loginSuccess by "Sikeres belépés"
    val loginFail by "Sikertelen belépés"

    val accountNotFound by "Nem létező fiók"

    val logout by "Kilépés"
    val grantRole by "Szerepkör megadása"
    val revokeRole by "Szerepkör visszavonása"

    val setLocked by "Fiók zárolás"

    val securityPolicy by "Biztonsági házirend"
    val securityPolicySupport by securityPolicy.support("Biztonsági szabályok beállítása. Jelszóerősség, hibás bejelentkezések száma stb.")

}
