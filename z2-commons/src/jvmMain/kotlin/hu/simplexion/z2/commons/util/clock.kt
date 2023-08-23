package hu.simplexion.z2.commons.util

actual fun vmNowMicro(): Long {
    return System.nanoTime() / 1_000
}