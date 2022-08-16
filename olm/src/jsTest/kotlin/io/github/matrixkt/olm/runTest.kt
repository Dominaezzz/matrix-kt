package io.github.matrixkt.olm

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.promise


actual fun BaseTest.runTest(block: suspend CoroutineScope.() -> Unit): dynamic = GlobalScope.promise {
    JsOlm.init()
    block()
}
