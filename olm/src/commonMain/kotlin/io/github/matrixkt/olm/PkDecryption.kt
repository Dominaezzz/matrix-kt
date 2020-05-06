package io.github.matrixkt.olm

expect class PkDecryption {
    val publicKey: String

    fun clear()

    val privateKey: ByteArray

    fun decrypt(message: PkMessage): String

    fun pickle(key: ByteArray): String

    companion object {
        val publicKeyLength: Long
        val privateKeyLength: Long

        fun fromPrivate(privateKey: ByteArray): PkDecryption

        fun unpickle(key: ByteArray, pickle: String): PkDecryption
    }
}
