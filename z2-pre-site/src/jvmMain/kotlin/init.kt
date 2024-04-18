import hu.simplexion.z2.auth.util.runTransactionAsSecurityOfficer
import hu.simplexion.z2.localization.impl.LocaleImpl.Companion.localeImpl
import hu.simplexion.z2.localization.model.Locale

fun init() {

    runTransactionAsSecurityOfficer { context ->

        val impl = localeImpl(context)

        if (impl.list().isNotEmpty()) return@runTransactionAsSecurityOfficer

        val hu = impl(context).add(Locale().also {
            it.isoCode = "hu"
            it.countryCode = "HU"
            it.nativeName = "Magyar"
            it.priority = 10
        })

        val de = impl.add(Locale().also {
            it.isoCode = "de"
            it.countryCode = "DE"
            it.nativeName = "Deutsch"
        })

        val en =impl.add(Locale().also {
            it.isoCode = "en"
            it.countryCode = "US"
            it.nativeName = "English"
        })

//        impl.load(hu, Files.readAllBytes(Paths.get("./var/init/localization.kotlin_hu_simplexion_z2_z2_site.txt")))
//        impl.load(de, Files.readAllBytes(Paths.get("./var/init/localization.kotlin_hu_simplexion_z2_z2_site.txt")))
//        impl.load(en, Files.readAllBytes(Paths.get("./var/init/localization.kotlin_hu_simplexion_z2_z2_site.txt")))

    }
}