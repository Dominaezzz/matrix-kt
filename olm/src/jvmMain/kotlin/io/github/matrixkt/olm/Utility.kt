package io.github.matrixkt.olm

import colm.internal.NativeSize
import colm.internal.OlmLibrary.olm_clear_utility
import colm.internal.OlmLibrary.olm_ed25519_verify
import colm.internal.OlmLibrary.olm_sha256
import colm.internal.OlmLibrary.olm_sha256_length
import colm.internal.OlmLibrary.olm_utility
import colm.internal.OlmLibrary.olm_utility_last_error
import colm.internal.OlmLibrary.olm_utility_size
import com.sun.jna.Native
import com.sun.jna.Pointer

/**
 * Olm SDK helper class.
 */
public actual class Utility {
    private val ptr = genericInit(::olm_utility, ::olm_utility_size)

    public actual fun clear() {
        olm_clear_utility(ptr)
        Native.free(Pointer.nativeValue(ptr.pointer))
    }

    /**
     * Verify an ed25519 signature.
     *
     * An exception is thrown if the operation fails.
     * @param key the ed25519 key (fingerprint key)
     * @param message the signed message
     * @param signature the base64-encoded message signature to be checked.
     */
    public actual fun verifyEd25519Signature(key: String, message: String, signature: String) {
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
    public actual fun sha256(input: String): String {
        val outputLength = olm_sha256_length(ptr)
        return withAllocation(outputLength.toLong()) { output ->
            input.withNativeRead { inputPtr, inputLen ->
                val result = olm_sha256(ptr, inputPtr, inputLen, output, outputLength)
                checkError(result)
            }
            output.toKString(outputLength.toInt())
        }
    }

    private fun checkError(result: NativeSize) {
        genericCheckError(ptr, result, ::olm_utility_last_error)
    }
}
