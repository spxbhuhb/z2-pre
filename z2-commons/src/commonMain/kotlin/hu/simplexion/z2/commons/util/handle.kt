package hu.simplexion.z2.commons.util

typealias Z2Handle = Long

/**
 * Returns with a long that is unique during the lifetime of the virtual
 * machine. If it is not possible to get a new unique handle throws
 * an exception.
 *
 * This function is thread safe. In JVM it uses an AtomicLong, in JS it
 * is simply a number incremented.
 *
 * @throws RuntimeException when out of handles
 */
expect fun nextHandle() : Z2Handle