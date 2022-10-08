package io.github.matrixkt.olm

actual inline fun withOlmInit(crossinline block: () -> Unit) {
    block()
}
