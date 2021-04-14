package io.github.matrixkt.olm

import colm.internal.*
import kotlinx.cinterop.*
import platform.posix.size_t
import kotlin.random.Random

public actual class PkEncryption actual constructor(recipientKey: String) {
    private val ptr = genericInit(::olm_pk_encryption, ::olm_pk_encryption_size)

    init {
        try {
            recipientKey.withNativeRead { keyPtr, keyLen ->
                val result = olm_pk_encryption_set_recipient_key(ptr, keyPtr, keyLen)
                checkError(result)
            }
        } catch (e: Exception) {
            clear()
            throw e
        }
    }

    public actual fun clear() {
        olm_clear_pk_encryption(ptr)
        nativeHeap.free(ptr)
    }

    public actual fun encrypt(plaintext: String, random: Random): PkMessage {
        return plaintext.withNativeRead { plaintextPtr, plaintextLen ->
            val cipherTextLength = olm_pk_ciphertext_length(ptr, plaintextLen.convert())
            val macLength = olm_pk_mac_length(ptr)
            val ephemeralLength = olm_pk_key_length()

            val cipherText = ByteArray(cipherTextLength.convert())
            val mac = ByteArray(macLength.convert())
            val ephemeral = ByteArray(ephemeralLength.convert())

            val randomLength = olm_pk_encrypt_random_length(ptr)
            withRandomBuffer(randomLength, random) { randomBuff ->
                mac[mac.lastIndex] = 0
                ephemeral[ephemeral.lastIndex] = 0

                val result = olm_pk_encrypt(ptr, plaintextPtr, plaintextLen,
                    cipherText.refTo(0), cipherTextLength,
                    mac.refTo(0), macLength,
                    ephemeral.refTo(0), ephemeralLength,
                    randomBuff, randomLength)
                checkError(result)
            }
            PkMessage(cipherText.decodeToString(), mac.decodeToString(), ephemeral.decodeToString())
        }
    }

    private fun checkError(result: size_t) {
        genericCheckError(ptr, result, ::olm_pk_encryption_last_error)
    }
}
