package testutils

import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.promise

@OptIn(DelicateCoroutinesApi::class)
actual fun runSuspendTest(block: suspend () -> Unit): dynamic = GlobalScope.promise {
    block()
}
