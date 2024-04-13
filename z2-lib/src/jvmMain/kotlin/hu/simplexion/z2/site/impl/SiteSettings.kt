package hu.simplexion.z2.site.impl

import hu.simplexion.z2.setting.dsl.SettingFlag
import hu.simplexion.z2.setting.dsl.setting
import java.nio.file.Path

object SiteSettings {

    var protocol by setting<String> { "SITE_PROTOCOL" }
    var host by setting<String> { "SITE_HOST" }
    var port by setting<Int> { "SITE_PORT" }

    var name by setting<String> { "SITE_NAME" }

    var static by setting<Path> { "SITE_STATIC" }

    var test by setting<Boolean> { "SITE_TEST" } default true
    var testEmailAddress by setting<String> { "TEST_EMAIL_ADDRESS" }
    var testPassword by setting<String> {"TEST_PASSWORD" } .. SettingFlag.sensitive

}