package io.github.matrixkt.olm

import colm.internal.*
import kotlinx.cinterop.*
import platform.posix.size_t

class PkDecryption private constructor() {
    private val ptr = genericInit(::olm_pk_decryption, ::olm_pk_decryption_size)

    fun clear() {
        olm_clear_pk_decryption(ptr)
        nativeHeap.free(ptr)
    }

    val privateKey: ByteArray
        get() {
            val privateKeyLength = olm_pk_private_key_length()
            val privateKey = ByteArray(privateKeyLength.convert())

            val result = olm_pk_get_private_key(ptr, privateKey.refTo(0), privateKeyLength)
            checkError(result)
            return privateKey
        }

    fun decrypt(message: PkMessage): String {
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

    fun pickle(key: ByteArray): String {
        return genericPickle(ptr, key, ::olm_pickle_pk_decryption_length, ::olm_pickle_pk_decryption, ::checkError)
    }

    private fun checkError(result: size_t) {
        genericCheckError(ptr, result, ::olm_pk_decryption_last_error)
    }

    companion object {
        val publicKeyLength: Long get() = olm_pk_key_length().convert()
        val privateKeyLength: Long get() = olm_pk_private_key_length().convert()

        private inline fun create(block: PkDecryption.(CValuesRef<*>, size_t) -> Unit): Pair<PkDecryption, String> {
            val publicKeyLength = olm_pk_key_length()
            val publicKey = ByteArray(publicKeyLength.convert())

            val obj = PkDecryption()
            try {
                obj.block(publicKey.refTo(0), publicKeyLength)
            } catch (e: Exception) {
                obj.clear() // Prevent leak
                throw e
            }
            return obj to publicKey.decodeToString()
        }

        fun fromPrivate(privateKey: ByteArray): Pair<PkDecryption, String> {
            return create { publicKey, publicKeyLength ->
                val result = olm_pk_key_from_private(ptr,
                    publicKey, publicKeyLength,
                    privateKey.refTo(0), privateKey.size.convert())
                checkError(result)
            }
        }

        fun unpickle(key: ByteArray, pickle: String): Pair<PkDecryption, String> {
            return create { publicKey, publicKeyLength ->
                genericUnpickle(ptr, key, pickle, { ptr, key, keyLen, pickle, pickleLen ->
                    olm_unpickle_pk_decryption(ptr, key, keyLen, pickle, pickleLen, publicKey, publicKeyLength)
                }, ::checkError)
            }
        }
    }
}
