package io.github.matrixkt.olm

import kotlin.random.Random

public expect class PkEncryption(recipientKey: String) {
    public fun clear()

    public fun encrypt(plaintext: String, random: Random = Random.Default): PkMessage
}
