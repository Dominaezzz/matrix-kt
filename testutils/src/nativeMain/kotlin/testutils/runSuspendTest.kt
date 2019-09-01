package testutils

import kotlinx.coroutines.runBlocking

actual fun runSuspendTest(block: suspend () -> Unit) = runBlocking {
    block()
}
