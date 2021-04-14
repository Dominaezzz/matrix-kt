package io.github.matrixkt.olm

public data class PkMessage(
    val cipherText: String,
    val mac: String,
    val ephemeralKey: String
)
