package io.github.matrixkt.olm

import colm.internal.*
import platform.posix.size_t
import kotlinx.cinterop.*

/**
 * Olm SDK helper class.
 */
actual class Utility {
    private val ptr = genericInit(::olm_utility, ::olm_utility_size)

    actual fun clear() {
        olm_clear_utility(ptr)
        nativeHeap.free(ptr)
    }

    /**
     * Verify an ed25519 signature.
     *
     * An exception is thrown if the operation fails.
     * @param key the ed25519 key (fingerprint key)
     * @param message the signed message
     * @param signature the base64-encoded message signature to be checked.
     */
    actual fun verifyEd25519Signature(key: String, message: String, signature: String) {
        key.withNativeRead { keyPtr, keyLength ->
            message.withNativeRead { messagePtr, messageLength ->
                signature.withNativeRead { signaturePtr, signatureLen ->
                    val result = olm_ed25519_verify(ptr, keyPtr, keyLength, messagePtr, messageLength, signaturePtr, signatureLen)
                    checkError(result)
                }
            }
        }
    }

    /**
     * Compute the hash(SHA-256) value of the string given in parameter.
     *
     * The hash value is the returned by the method.
     * @param input message to be hashed
     * @return hash value if operation succeed, null otherwise
     */
    actual fun sha256(input: String): String {
        val outputLength = olm_sha256_length(ptr)
        val output = ByteArray(outputLength.convert())
        input.withNativeRead { inputPtr, inputLen ->
            output.usePinned {
                val result = olm_sha256(ptr, inputPtr, inputLen, it.addressOf(0), outputLength)
                checkError(result)
            }
        }
        return output.decodeToString()
    }

    private fun checkError(result: size_t) {
        genericCheckError(ptr, result, ::olm_utility_last_error)
    }
}
