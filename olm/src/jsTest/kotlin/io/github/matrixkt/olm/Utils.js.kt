package io.github.matrixkt.olm

actual inline fun withOlmInit(crossinline block: () -> Unit): dynamic = JsOlm.init().then { block() }
