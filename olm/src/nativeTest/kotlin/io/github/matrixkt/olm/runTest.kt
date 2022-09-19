package io.github.matrixkt.olm

import kotlinx.coroutines.CoroutineScope

actual fun BaseTest.runTest(block: () -> Unit) {
    block()
}
