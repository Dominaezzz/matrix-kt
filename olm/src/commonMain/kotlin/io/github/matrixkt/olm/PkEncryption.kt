package io.github.matrixkt.olm

import kotlin.random.Random

expect class PkEncryption(recipientKey: String) {
    fun clear()

    fun encrypt(plaintext: String, random: Random = Random.Default): PkMessage
}
