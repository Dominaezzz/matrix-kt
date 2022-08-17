package io.github.matrixkt.olm

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.runBlocking

actual fun BaseTest.runTest(block: () -> Unit) = runBlocking {
    block()
}
