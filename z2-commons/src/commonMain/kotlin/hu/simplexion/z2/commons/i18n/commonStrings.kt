package hu.simplexion.z2.commons.i18n

import hu.simplexion.z2.commons.util.UUID

object commonStrings : LocalizedTextStore(UUID("7daef093-c00e-41dc-bc52-8848aee321ee")) {
    val start by "start"
    val name by "név"
    val uuid by "uuid"
    val status by "állapot"
    val data by "adat"
    val add by "hozzáadás"
    val remove by "eltávolítás"
    val update by "változtatás"
    val enabled by "engedélyezve"
    val path by "útvonal"
    val save by "mentés"
    val cancel by "mégse"

    val monday by "hétfő"
    val tuesday by "kedd"
    val wednesday by "szerda"
    val thursday by "csütörtök"
    val friday by "péntek"
    val saturday by "szombat"
    val sunday by "vasárnap"

    val january by "január"
    val february by "február"
    val march by "március"
    val april by "április"
    val may by "május"
    val june by "június"
    val july by "július"
    val august by "augusztus"
    val september by "szeptember"
    val october by "október"
    val november by "november"
    val december by "december"

    val januaryShort by "jan"
    val februaryShort by "feb"
    val marchShort by "már"
    val aprilShort by "ápr"
    val mayShort by "máj"
    val juneShort by "jún"
    val julyShort by "júl"
    val augustShort by "aug"
    val septemberShort by "szep"
    val octoberShort by "okt"
    val novemberShort by "nov"
    val decemberShort by "dec"

    val localDateSupportText by "YYYY/MM/DD"
}