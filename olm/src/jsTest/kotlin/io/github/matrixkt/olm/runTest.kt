package io.github.matrixkt.olm

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.promise


actual fun runTest(block: () -> Unit): dynamic = JsOlm.init().then { block() }
