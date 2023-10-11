package hu.simplexion.z2.commons.util

private var lastIssuedHandle = 0L

actual fun nextHandle() : Z2Handle {
    if (lastIssuedHandle == Long.MAX_VALUE) throw RuntimeException("out of handles")
    return ++lastIssuedHandle
}