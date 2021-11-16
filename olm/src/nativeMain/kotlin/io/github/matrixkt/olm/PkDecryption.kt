package io.github.matrixkt.olm

import colm.internal.*
import kotlinx.cinterop.*
import platform.posix.size_t
import kotlin.random.Random

public actual class PkDecryption {
    private val ptr: CPointer<OlmPkDecryption>
    public actual val publicKey: String

    private constructor(ptr: CPointer<OlmPkDecryption>, publicKey: String) {
        this.ptr = ptr
        this.publicKey = publicKey
    }

    public actual constructor(random: Random) {
        val publicKeyLength = olm_pk_key_length()
        val privateKeyLength = olm_pk_private_key_length()
        val publicKey = ByteArray(publicKeyLength.convert())

        ptr = genericInit(::olm_pk_decryption, ::olm_pk_decryption_size)
        try {
            withRandomBuffer(privateKeyLength, random) { privateKey ->
                val result = olm_pk_key_from_private(ptr,
                    publicKey.refTo(0), publicKeyLength,
                    privateKey, privateKeyLength)
                checkError(ptr, result)
            }
        } catch (e: Exception) {
            clear()
            throw e
        }
        this.publicKey = publicKey.decodeToString()
    }

    public actual fun clear() {
        olm_clear_pk_decryption(ptr)
        nativeHeap.free(ptr)
    }

    public actual val privateKey: ByteArray
        get() {
            val privateKeyLength = olm_pk_private_key_length()
            val privateKey = ByteArray(privateKeyLength.convert())

            val result = olm_pk_get_private_key(ptr, privateKey.refTo(0), privateKeyLength)
            checkError(result)
            return privateKey
        }

    public actual fun decrypt(message: PkMessage): String {
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

    public actual fun pickle(key: ByteArray): String {
        return genericPickle(ptr, key, ::olm_pickle_pk_decryption_length, ::olm_pickle_pk_decryption, ::checkError)
    }

    private fun checkError(result: size_t) {
        genericCheckError(ptr, result, ::olm_pk_decryption_last_error)
    }

    public actual companion object {
        public actual val publicKeyLength: Long get() = olm_pk_key_length().convert()
        public actual val privateKeyLength: Long get() = olm_pk_private_key_length().convert()

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

        public actual fun fromPrivate(privateKey: ByteArray): PkDecryption {
            return create { ptr, publicKey, publicKeyLength ->
                val result = olm_pk_key_from_private(ptr,
                    publicKey, publicKeyLength,
                    privateKey.refTo(0), privateKey.size.convert())
                checkError(ptr, result)
            }
        }

        public actual fun unpickle(key: ByteArray, pickle: String): PkDecryption {
            return create { ptr, publicKey, publicKeyLength ->
                genericUnpickle(ptr, key, pickle, { _ptr, key, keyLen, pickle, pickleLen ->
                    olm_unpickle_pk_decryption(_ptr, key, keyLen, pickle, pickleLen, publicKey, publicKeyLength)
                }, { checkError(ptr, it) })
            }
        }
    }
}
