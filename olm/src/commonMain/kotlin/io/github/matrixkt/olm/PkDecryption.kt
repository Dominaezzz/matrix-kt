package io.github.matrixkt.olm

import kotlin.random.Random

expect class PkDecryption(random: Random = Random.Default) {
    val publicKey: String

    fun clear()

    val privateKey: ByteArray

    fun decrypt(message: PkMessage): String

    fun pickle(key: ByteArray): String

    companion object {
        fun unpickle(key: ByteArray, pickle: String): PkDecryption
    }
}
