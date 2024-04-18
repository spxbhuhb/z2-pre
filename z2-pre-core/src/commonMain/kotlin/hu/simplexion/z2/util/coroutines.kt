package hu.simplexion.z2.util

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

/**
 * Creates a new coroutine scope in [Dispatchers.Default]
 * and launches the block in that scope.
 */
@PublicApi
fun localLaunch(block : suspend CoroutineScope.() -> Unit) : Job =
    CoroutineScope(Dispatchers.Default).launch(block = block)