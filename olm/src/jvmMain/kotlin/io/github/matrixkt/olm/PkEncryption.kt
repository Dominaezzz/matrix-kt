package io.github.matrixkt.olm

import colm.internal.NativeSize
import colm.internal.OlmLibrary.olm_clear_pk_encryption
import colm.internal.OlmLibrary.olm_pk_ciphertext_length
import colm.internal.OlmLibrary.olm_pk_encrypt
import colm.internal.OlmLibrary.olm_pk_encrypt_random_length
import colm.internal.OlmLibrary.olm_pk_encryption
import colm.internal.OlmLibrary.olm_pk_encryption_last_error
import colm.internal.OlmLibrary.olm_pk_encryption_set_recipient_key
import colm.internal.OlmLibrary.olm_pk_encryption_size
import colm.internal.OlmLibrary.olm_pk_key_length
import colm.internal.OlmLibrary.olm_pk_mac_length
import com.sun.jna.Native
import com.sun.jna.Pointer
import kotlin.random.Random

actual class PkEncryption {
    private val ptr = genericInit(::olm_pk_encryption, ::olm_pk_encryption_size)

    actual fun clear() {
        olm_clear_pk_encryption(ptr)
        Native.free(Pointer.nativeValue(ptr.pointer))
    }

    actual fun setRecipientKey(key: String) {
        key.withNativeRead { keyPtr, keyLen ->
            val result = olm_pk_encryption_set_recipient_key(ptr, keyPtr, keyLen)
            checkError(result)
        }
    }

    actual fun encrypt(plaintext: String, random: Random): PkMessage {
        return plaintext.withNativeRead { plaintextPtr, plaintextLen ->
            val cipherTextLength = olm_pk_ciphertext_length(ptr, plaintextLen)
            val macLength = olm_pk_mac_length(ptr)
            val ephemeralLength = olm_pk_key_length()

            withAllocation(cipherTextLength.toLong()) { cipherText ->
                withAllocation(macLength.toLong()) { mac ->
                    withAllocation(ephemeralLength.toLong()) { ephemeral ->
                        val randomLength = olm_pk_encrypt_random_length(ptr)
                        withRandomBuffer(randomLength, random) { randomBuff ->
                            val result = olm_pk_encrypt(ptr, plaintextPtr, plaintextLen,
                                cipherText, cipherTextLength,
                                mac, macLength,
                                ephemeral, ephemeralLength,
                                randomBuff, randomLength)
                            checkError(result)
                        }
                        PkMessage(
                            cipherText.toKString(cipherTextLength.toInt()),
                            mac.toKString(macLength.toInt()),
                            ephemeral.toKString(ephemeralLength.toInt())
                        )
                    }
                }
            }
        }
    }

    private fun checkError(result: NativeSize) {
        genericCheckError(ptr, result, ::olm_pk_encryption_last_error)
    }
}
