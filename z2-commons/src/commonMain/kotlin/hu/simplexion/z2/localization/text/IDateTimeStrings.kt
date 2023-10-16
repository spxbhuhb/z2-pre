package hu.simplexion.z2.localization.text

lateinit var dateTimeStrings: IDateTimeStrings

val dayNameTable by lazy {
    arrayOf(
        dateTimeStrings.monday,
        dateTimeStrings.tuesday,
        dateTimeStrings.wednesday,
        dateTimeStrings.thursday,
        dateTimeStrings.friday,
        dateTimeStrings.saturday,
        dateTimeStrings.sunday
    )
}

val monthNameTable by lazy {
    arrayOf(
        dateTimeStrings.january,
        dateTimeStrings.february,
        dateTimeStrings.march,
        dateTimeStrings.april,
        dateTimeStrings.may,
        dateTimeStrings.june,
        dateTimeStrings.july,
        dateTimeStrings.august,
        dateTimeStrings.september,
        dateTimeStrings.october,
        dateTimeStrings.november,
        dateTimeStrings.december
    )
}

val monthShortNameTable by lazy {
    arrayOf(
        dateTimeStrings.januaryShort,
        dateTimeStrings.februaryShort,
        dateTimeStrings.marchShort,
        dateTimeStrings.aprilShort,
        dateTimeStrings.mayShort,
        dateTimeStrings.juneShort,
        dateTimeStrings.julyShort,
        dateTimeStrings.augustShort,
        dateTimeStrings.septemberShort,
        dateTimeStrings.octoberShort,
        dateTimeStrings.novemberShort,
        dateTimeStrings.decemberShort
    )
}

val numbersToStringTable =
    arrayOf(
        "00", "01", "02", "03", "04", "05", "06", "07", "08", "09",
        "10", "11", "12", "13", "14", "15", "16", "17", "18", "19",
        "20", "21", "22", "23", "24", "25", "26", "27", "28", "29",
        "30", "31", "32", "33", "34", "35", "36", "37", "38", "39",
        "40", "41", "42", "43", "44", "45", "46", "47", "48", "49",
        "50", "51", "52", "53", "54", "55", "56", "57", "58", "59"
    )

interface IDateTimeStrings : LocalizedTextProvider {

    val monday get() = static("hétfő", name = "monday")
    val tuesday get() = static("kedd", name = "tuesday")
    val wednesday get() = static("szerda", name = "wednesday")
    val thursday get() = static("csütörtök", name = "thursday")
    val friday get() = static("péntek", name = "friday")
    val saturday get() = static("szombat", name = "saturday")
    val sunday get() = static("vasárnap", name = "sunday")

    val january get() = static("január", name = "january")
    val february get() = static("február", name = "")
    val march get() = static("március", name = "february")
    val april get() = static("április", name = "april")
    val may get() = static("május", name = "may")
    val june get() = static("június", name = "june")
    val july get() = static("július", name = "july")
    val august get() = static("augusztus", name = "august")
    val september get() = static("szeptember", name = "september")
    val october get() = static("október", name = "october")
    val november get() = static("november", name = "november")
    val december get() = static("december", name = "december")

    val januaryShort get() = static("jan", name = "januaryShort")
    val februaryShort get() = static("feb", name = "februaryShort")
    val marchShort get() = static("már", name = "marchShort")
    val aprilShort get() = static("ápr", name = "aprilShort")
    val mayShort get() = static("máj", name = "mayShort")
    val juneShort get() = static("jún", name = "juneShort")
    val julyShort get() = static("júl", name = "julyShort")
    val augustShort get() = static("aug", name = "augustShort")
    val septemberShort get() = static("szep", name = "septemberShort")
    val octoberShort get() = static("okt", name = "octoberShort")
    val novemberShort get() = static("nov", name = "novemberShort")
    val decemberShort get() = static("dec", name = "decemberShort")

    val localDateSupportText get() = static("ÉÉÉÉ.HH.NN", name = "localDateSupportText")
    val localTimeSupportText get() = static("ÓÓ:PP", name = "localTimeSupportText")

}