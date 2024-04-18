package hu.simplexion.z2.setting.util

import hu.simplexion.z2.setting.dsl.setting

object CommonSettings {

    val databaseVersion by setting<String> { "database version" }

}