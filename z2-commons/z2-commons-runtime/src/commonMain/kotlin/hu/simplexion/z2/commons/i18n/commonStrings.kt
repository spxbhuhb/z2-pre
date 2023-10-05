package hu.simplexion.z2.commons.i18n

val a = dateTimeStrings.april

object dateTimeStrings : DateTimeStrings

interface LocalizedTextProvider

interface DateTimeStrings : LocalizedTextProvider {

    val monday get() = "hétfő"
    val tuesday get() = "kedd"
    val wednesday get() = "szerda"
    val thursday get() = "csütörtök"
    val friday get() = "péntek"
    val saturday get() = "szombat"
    val sunday get() = "vasárnap"

    val january get() = "január"
    val february get() = "február"
    val march get() = "március"
    val april get() = "április"
    val may get() = "május"
    val june get() = "június"
    val july get() = "július"
    val august get() = "augusztus"
    val september get() = "szeptember"
    val october get() = "október"
    val november get() = "november"
    val december get() = "december"

    val januaryShort get() = "jan"
    val februaryShort get() = "feb"
    val marchShort get() = "már"
    val aprilShort get() = "ápr"
    val mayShort get() = "máj"
    val juneShort get() = "jún"
    val julyShort get() = "júl"
    val augustShort get() = "aug"
    val septemberShort get() = "szep"
    val octoberShort get() = "okt"
    val novemberShort get() = "nov"
    val decemberShort get() = "dec"

    val localDateSupportText get() = "ÉÉÉÉ.HH.NN"
    val localTimeSupportText get() = "ÓÓ:PP"

}

object commonStrings : LocalizedTextStore() {
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

    val localDateSupportText by "ÉÉÉÉ.HH.NN"
    val localTimeSupportText by "ÓÓ:PP"

}