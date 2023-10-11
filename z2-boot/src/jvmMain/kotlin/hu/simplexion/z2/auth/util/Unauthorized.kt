package hu.simplexion.z2.auth.util

class Unauthorized(val reason : String, val locked : Boolean = false) : RuntimeException(reason)