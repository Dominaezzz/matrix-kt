package io.github.matrixkt.olm

data class PkMessage(
    val cipherText: String,
    val mac: String,
    val ephemeralKey: String
)
