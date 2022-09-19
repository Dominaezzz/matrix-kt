package io.github.matrixkt.olm

import kotlinx.coroutines.CoroutineScope

actual fun runTest(block: () -> Unit) {
    block()
}
