package io.github.matrixkt.olm

import colm.internal.*
import kotlinx.cinterop.*
import platform.posix.size_t

actual class PkDecryption private constructor(private val ptr: CPointer<OlmPkDecryption>, actual val publicKey: String) {
    actual fun clear() {
        olm_clear_pk_decryption(ptr)
        nativeHeap.free(ptr)
    }

    actual val privateKey: ByteArray
        get() {
            val privateKeyLength = olm_pk_private_key_length()
            val privateKey = ByteArray(privateKeyLength.convert())

            val result = olm_pk_get_private_key(ptr, privateKey.refTo(0), privateKeyLength)
            checkError(result)
            return privateKey
        }

    actual fun decrypt(message: PkMessage): String {
        return message.mac.withNativeRead { macPtr, macLen ->
            message.ephemeralKey.withNativeRead { ephemeralKeyPtr, ephemeralKeyLen ->
                message.cipherText.withNativeRead { cipherTextPtr, cipherTextLen ->
                    val maxPlainTextLength = olm_pk_max_plaintext_length(ptr, cipherTextLen.convert())

                    val plainText = ByteArray(maxPlainTextLength.convert())

                    val plainTextLength = olm_pk_decrypt(ptr,
                        ephemeralKeyPtr, ephemeralKeyLen.convert(),
                        macPtr, macLen.convert(),
                        cipherTextPtr, cipherTextLen.convert(),
                        plainText.refTo(0), maxPlainTextLength)
                    checkError(plainTextLength)

                    plainText.decodeToString(endIndex = plainTextLength.convert())
                }
            }
        }
    }

    actual fun pickle(key: ByteArray): String {
        return genericPickle(ptr, key, ::olm_pickle_pk_decryption_length, ::olm_pickle_pk_decryption, ::checkError)
    }

    private fun checkError(result: size_t) {
        genericCheckError(ptr, result, ::olm_pk_decryption_last_error)
    }

    actual companion object {
        actual val publicKeyLength: Long get() = olm_pk_key_length().convert()
        actual val privateKeyLength: Long get() = olm_pk_private_key_length().convert()

        private inline fun create(block: (CPointer<OlmPkDecryption>, CValuesRef<*>, size_t) -> Unit): PkDecryption {
            val publicKeyLength = olm_pk_key_length()
            val publicKey = ByteArray(publicKeyLength.convert())

            val obj = genericInit(::olm_pk_decryption, ::olm_pk_decryption_size)
            try {
                block(obj, publicKey.refTo(0), publicKeyLength)
            } catch (e: Exception) {
                // Prevent leak
                olm_clear_pk_decryption(obj)
                nativeHeap.free(obj)
                throw e
            }
            return PkDecryption(obj, publicKey.decodeToString())
        }

        private fun checkError(ptr: CPointer<OlmPkDecryption>, result: size_t) {
            genericCheckError(ptr, result, ::olm_pk_decryption_last_error)
        }

        actual fun fromPrivate(privateKey: ByteArray): PkDecryption {
            return create { ptr, publicKey, publicKeyLength ->
                val result = olm_pk_key_from_private(ptr,
                    publicKey, publicKeyLength,
                    privateKey.refTo(0), privateKey.size.convert())
                checkError(ptr, result)
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
