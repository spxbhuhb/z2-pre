package hu.simplexion.z2.util

import java.util.concurrent.atomic.AtomicLong

private val lastIssuedHandle = AtomicLong(0L)

actual fun nextHandle() : Z2Handle {
    var lastValue : Long

    do {
        lastValue = lastIssuedHandle.get()
        if (lastValue == Long.MAX_VALUE) {
           throw RuntimeException("out of handles")
        }
    } while (! lastIssuedHandle.compareAndSet(lastValue, lastValue + 1))

    return lastValue
}