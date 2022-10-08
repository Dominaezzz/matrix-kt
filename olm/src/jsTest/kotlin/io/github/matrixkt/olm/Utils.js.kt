package io.github.matrixkt.olm

actual inline fun runTest(crossinline block: () -> Unit): dynamic = JsOlm.init().then { block() }
