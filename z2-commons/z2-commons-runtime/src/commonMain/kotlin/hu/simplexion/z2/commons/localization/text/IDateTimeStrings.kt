package hu.simplexion.z2.commons.localization.text

object dateTimeStrings : IDateTimeStrings {
    
    val dayNameTable =
        arrayOf(monday, tuesday, wednesday, thursday, friday, saturday, sunday)

    val monthNameTable =
        arrayOf(january, february, march, april, may, june, july, august, september, october, november, december)

    val monthShortNameTable =
        arrayOf(januaryShort, februaryShort, marchShort, aprilShort, mayShort, juneShort, julyShort, augustShort, septemberShort, octoberShort, novemberShort, decemberShort)

    val numbersToStringTable =
        arrayOf(
            "00", "01", "02", "03", "04", "05", "06", "07", "08", "09",
            "10", "11", "12", "13", "14", "15", "16", "17", "18", "19",
            "20", "21", "22", "23", "24", "25", "26", "27", "28", "29",
            "30", "31", "32", "33", "34", "35", "36", "37", "38", "39",
            "40", "41", "42", "43", "44", "45", "46", "47", "48", "49",
            "50", "51", "52", "53", "54", "55", "56", "57", "58", "59"
        )

}

interface IDateTimeStrings : LocalizedTextProvider {

    val monday get() = static("hétfő")
    val tuesday get() = static("kedd")
    val wednesday get() = static("szerda")
    val thursday get() = static("csütörtök")
    val friday get() = static("péntek")
    val saturday get() = static("szombat")
    val sunday get() = static("vasárnap")

    val january get() = static("január")
    val february get() = static("február")
    val march get() = static("március")
    val april get() = static("április")
    val may get() = static("május")
    val june get() = static("június")
    val july get() = static("július")
    val august get() = static("augusztus")
    val september get() = static("szeptember")
    val october get() = static("október")
    val november get() = static("november")
    val december get() = static("december")

    val januaryShort get() = static("jan")
    val februaryShort get() = static("feb")
    val marchShort get() = static("már")
    val aprilShort get() = static("ápr")
    val mayShort get() = static("máj")
    val juneShort get() = static("jún")
    val julyShort get() = static("júl")
    val augustShort get() = static("aug")
    val septemberShort get() = static("szep")
    val octoberShort get() = static("okt")
    val novemberShort get() = static("nov")
    val decemberShort get() = static("dec")

    val localDateSupportText get() = static("ÉÉÉÉ.HH.NN")
    val localTimeSupportText get() = static("ÓÓ:PP")

}