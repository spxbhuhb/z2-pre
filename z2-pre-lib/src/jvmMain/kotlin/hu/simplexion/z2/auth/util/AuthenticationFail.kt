package hu.simplexion.z2.auth.util

/**
 * Thrown on authentication fail.
 */
class AuthenticationFail(val reason : String, val locked : Boolean = false) : RuntimeException(reason)