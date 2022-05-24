package io.github.matrixkt.olm

import kotlin.random.Random

public actual class PkEncryption actual constructor(recipientKey: String) {
    private val ptr = JsOlm.PkEncryption()

    init {
        try {
            ptr.set_recipient_key(recipientKey)
        } catch (e: Exception) {
            clear()
            throw e
        }
    }

    public actual fun clear() {
        ptr.free()
    }

    public actual fun encrypt(plaintext: String, random: Random): PkMessage {
        return ptr.encrypt(plaintext)
    }

}
