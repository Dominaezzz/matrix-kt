package io.github.matrixkt.olm

import colm.internal.*
import colm.internal.OlmLibrary.olm_clear_pk_decryption
import colm.internal.OlmLibrary.olm_pickle_pk_decryption
import colm.internal.OlmLibrary.olm_pickle_pk_decryption_length
import colm.internal.OlmLibrary.olm_pk_decrypt
import colm.internal.OlmLibrary.olm_pk_decryption
import colm.internal.OlmLibrary.olm_pk_decryption_last_error
import colm.internal.OlmLibrary.olm_pk_decryption_size
import colm.internal.OlmLibrary.olm_pk_get_private_key
import colm.internal.OlmLibrary.olm_pk_key_from_private
import colm.internal.OlmLibrary.olm_pk_key_length
import colm.internal.OlmLibrary.olm_pk_max_plaintext_length
import colm.internal.OlmLibrary.olm_pk_private_key_length
import colm.internal.OlmLibrary.olm_unpickle_pk_decryption
import com.sun.jna.Native
import com.sun.jna.Pointer

actual class PkDecryption private constructor(private val ptr: OlmPkDecryption, actual val publicKey: String) {
    actual fun clear() {
        olm_clear_pk_decryption(ptr)
        Native.free(Pointer.nativeValue(ptr.pointer))
    }

    actual val privateKey: ByteArray
        get() {
            val privateKeyLength = olm_pk_private_key_length()
            val privateKey = ByteArray(privateKeyLength.toInt())
            privateKey.withNativeWrite {
                val result = olm_pk_get_private_key(ptr, it, privateKeyLength)
                checkError(result)
            }
            return privateKey
        }

    actual fun decrypt(message: PkMessage): String {
        return message.mac.withNativeRead { macPtr, macLen ->
            message.ephemeralKey.withNativeRead { ephemeralKeyPtr, ephemeralKeyLen ->
                message.cipherText.withNativeRead { cipherTextPtr, cipherTextLen ->
                    val maxPlainTextLength = olm_pk_max_plaintext_length(ptr, cipherTextLen)

                    withAllocation(maxPlainTextLength.toLong()) { plainText ->
                        val plainTextLength = olm_pk_decrypt(ptr,
                            ephemeralKeyPtr, ephemeralKeyLen,
                            macPtr, macLen,
                            cipherTextPtr, cipherTextLen,
                            plainText, maxPlainTextLength)
                        checkError(plainTextLength)

                        plainText.toKString(plainTextLength.toInt())
                    }
                }
            }
        }
    }

    actual fun pickle(key: ByteArray): String {
        return genericPickle(ptr, key, ::olm_pickle_pk_decryption_length, ::olm_pickle_pk_decryption, ::checkError)
    }

    private fun checkError(result: NativeSize) {
        genericCheckError(ptr, result, ::olm_pk_decryption_last_error)
    }

    actual companion object {
        actual val publicKeyLength: Long get() = olm_pk_key_length().toLong()
        actual val privateKeyLength: Long get() = olm_pk_private_key_length().toLong()

        private inline fun create(block: (OlmPkDecryption, Pointer, NativeSize) -> Unit): PkDecryption {
            val publicKeyLength = olm_pk_key_length()
            return withAllocation(publicKeyLength.toLong()) { publicKey ->
                val obj = genericInit(::olm_pk_decryption, ::olm_pk_decryption_size)
                try {
                    block(obj, publicKey, publicKeyLength)
                } catch (e: Exception) {
                    // Prevent leak
                    olm_clear_pk_decryption(obj)
                    Native.free(Pointer.nativeValue(obj.pointer))
                    throw e
                }
                PkDecryption(obj, publicKey.toKString(publicKeyLength.toInt()))
            }
        }

        private fun checkError(ptr: OlmPkDecryption, result: NativeSize) {
            genericCheckError(ptr, result, ::olm_pk_decryption_last_error)
        }

        actual fun fromPrivate(privateKey: ByteArray): PkDecryption {
            return create { ptr, publicKey, publicKeyLength ->
                privateKey.withNativeRead {
                    val result = olm_pk_key_from_private(ptr,
                        publicKey, publicKeyLength,
                        it, NativeSize(privateKey.size))
                    checkError(ptr, result)
                }
            }
        }

        actual fun unpickle(key: ByteArray, pickle: String): PkDecryption {
            return create { ptr, publicKey, publicKeyLength ->
                genericUnpickle(ptr, key, pickle, { ptr, key, keyLen, pickle, pickleLen ->
                    olm_unpickle_pk_decryption(ptr, key, keyLen, pickle, pickleLen, publicKey, publicKeyLength)
                }, { checkError(ptr, it) })
            }
        }
    }
}
