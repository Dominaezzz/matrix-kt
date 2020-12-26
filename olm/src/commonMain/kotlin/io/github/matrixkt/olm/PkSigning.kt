package io.github.matrixkt.olm


expect class PkSigning(seed: ByteArray) {
    val publicKey: String

    fun clear()

    fun sign(message: String): String

    companion object {
        val seedLength: Long
    }
}
