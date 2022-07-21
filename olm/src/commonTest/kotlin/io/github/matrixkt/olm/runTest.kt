package io.github.matrixkt.olm
import kotlinx.coroutines.CoroutineScope

expect fun BaseTest.runTest(block: suspend CoroutineScope.() -> Unit)
