package io.github.matrixkt.olm

actual fun runTest(block: () -> Unit): dynamic = JsOlm.init().then { block() }
