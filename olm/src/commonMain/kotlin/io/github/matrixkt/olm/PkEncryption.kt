package io.github.matrixkt.olm

import kotlin.random.Random

expect class PkEncryption() {
    fun clear()

    fun setRecipientKey(key: String)

    fun encrypt(plaintext: String, random: Random = Random.Default): PkMessage
}
