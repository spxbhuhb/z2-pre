package hu.simplexion.z2.setting.implementation

import hu.simplexion.z2.util.UUID
import kotlin.reflect.KProperty

val ENVIRONMENT = 1
val PROPERTIES = 1
val DATABASE = 1

val dbUrl by setting { "jdbc:postgresql://127.0.0.1/z2site" autoUpdate true sensitive true sources anyOf(ENVIRONMENT, PROPERTIES, DATABASE) }

val dbUrl4 by setting {
    path = "jdbc:postgresql://127.0.0.1/z2site"
    autoUpdate = true
    sensitive = true
    sources = anyOf(ENVIRONMENT, PROPERTIES, DATABASE)
}


val dbUrl2 by setting("jdbc:postgresql://127.0.0.1/z2site", autoUpdate = true, sensitive = true, sources = intArrayOf(ENVIRONMENT, PROPERTIES, DATABASE))

val dbUrl3 by setting(
    path = "jdbc:postgresql://127.0.0.1/z2site",
    autoUpdate = true,
    sensitive = true,
    sources = intArrayOf(ENVIRONMENT, PROPERTIES, DATABASE)
)

fun setting(path: String, autoUpdate: Boolean, sensitive: Boolean, sources: IntArray): Setting {
    TODO()
}

class SettingBuilder {
    var path : String = ""
    var autoUpdate: Boolean = true
    var sensitive: Boolean = true
    var sources : IntArray = intArrayOf()
    val default : String = ""

    infix fun Any.autoUpdate(value: Any): SettingBuilder {
        autoUpdate = true
        return this@SettingBuilder
    }

    infix fun Any.sensitive(value: Any): SettingBuilder {
        autoUpdate = true
        return this@SettingBuilder
    }

    infix fun Any.sources(value: Any): SettingBuilder {
        autoUpdate = true
        return this@SettingBuilder
    }

    fun anyOf(vararg sources: Int): IntArray {
        return sources
    }
}

fun setting(builder: SettingBuilder.() -> Unit): Setting {
    TODO()
}

data class Setting(
    val path: String,
    val owner: UUID<String>? = null,
    val autoUpdate: Boolean = true,
    val sensitive: Boolean = false
) {

    operator fun getValue(thisRef: Any?, property: KProperty<*>): String {
        return "$thisRef, thank you for delegating '${property.name}' to me!"
    }

    operator fun setValue(thisRef: Any?, property: KProperty<*>, value: String) {
        println("$value has been assigned to '${property.name}' in $thisRef.")
    }

    infix fun autoUpdate(enabled: Boolean): Setting {
        return copy(autoUpdate = enabled)
    }

    infix fun sensitive(value: Boolean): Setting {
        return copy(sensitive = value)
    }

}