package hu.simplexion.z2.util

/**
 * Run the block if `this` is true.
 * @return  the value of `this`
 */
fun Boolean.alsoIf(block : (it : Boolean) -> Unit) : Boolean {
    if (this) block(this)
    return this
}