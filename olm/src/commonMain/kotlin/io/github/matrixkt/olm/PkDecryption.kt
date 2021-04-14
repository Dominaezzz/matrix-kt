package io.github.matrixkt.olm

import kotlin.random.Random

public expect class PkDecryption(random: Random = Random.Default) {
    public val publicKey: String

    public fun clear()

    public val privateKey: ByteArray

    public fun decrypt(message: PkMessage): String

    public fun pickle(key: ByteArray): String

    public companion object {
        public fun unpickle(key: ByteArray, pickle: String): PkDecryption
    }
}
