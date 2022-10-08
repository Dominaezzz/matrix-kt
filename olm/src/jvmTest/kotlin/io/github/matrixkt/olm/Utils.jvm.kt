package io.github.matrixkt.olm

actual inline fun runTest(crossinline block: () -> Unit) { block() }
