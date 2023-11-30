package hu.simplexion.z2.setting.util

var environmentVariablePrefix = "Z2"

val String.fromEnvironment : String?
    get() = System.getenv("${environmentVariablePrefix}_$this")

val String.fromEnvironmentMandatory : String
    get() = checkNotNull(this.fromEnvironment) { "the mandatory environment variable $this is missing" }

fun mandatoryEnvString(name: String): String =
    name.fromEnvironmentMandatory

fun mandatoryEnvInt(name: String): Int =
    checkNotNull(name.fromEnvironmentMandatory.toIntOrNull()) { "value of environment variable $name is invalid" }

fun mandatoryEnvBoolean(name: String): Boolean =
    checkNotNull(name.fromEnvironmentMandatory.toBooleanStrictOrNull()) { "value of environment variable $name is invalid"}
