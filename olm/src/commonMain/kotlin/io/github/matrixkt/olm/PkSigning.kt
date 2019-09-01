package io.github.matrixkt.olm


expect class PkSigning() {
    fun clear()

    fun fromSeed(seed: ByteArray): String

    fun sign(message: String): String

    companion object {
        val seedLength: Long
    }
}
