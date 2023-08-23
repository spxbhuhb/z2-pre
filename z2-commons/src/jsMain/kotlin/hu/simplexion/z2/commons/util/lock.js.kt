package hu.simplexion.z2.commons.util

/**
 * This is a copy of Ktor multiplatform lock as we clearly need it.
 * Only JVM and JS implementations are added at the moment, I'll add native
 * when it becomes important:
 *
 * https://github.com/ktorio/ktor/blob/master/ktor-utils/posix/src/io/ktor/util/LockNative.kt
 */
actual class Lock actual constructor() {
    actual fun lock() {
    }

    actual fun unlock() {
    }
}