package io.github.matrixkt.olm

import colm.internal.NativeSize
import colm.internal.OlmLibrary.olm_clear_sas
import colm.internal.OlmLibrary.olm_create_sas
import colm.internal.OlmLibrary.olm_create_sas_random_length
import colm.internal.OlmLibrary.olm_sas
import colm.internal.OlmLibrary.olm_sas_calculate_mac
import colm.internal.OlmLibrary.olm_sas_calculate_mac_long_kdf
import colm.internal.OlmLibrary.olm_sas_generate_bytes
import colm.internal.OlmLibrary.olm_sas_get_pubkey
import colm.internal.OlmLibrary.olm_sas_last_error
import colm.internal.OlmLibrary.olm_sas_mac_length
import colm.internal.OlmLibrary.olm_sas_pubkey_length
import colm.internal.OlmLibrary.olm_sas_set_their_key
import colm.internal.OlmLibrary.olm_sas_size
import com.sun.jna.Native
import com.sun.jna.Pointer
import kotlin.random.Random

actual class SAS actual constructor(random: Random) {
    private val ptr = genericInit(::olm_sas, ::olm_sas_size)

    init {
        val randomSize = olm_create_sas_random_length(ptr)
        val result = withRandomBuffer(randomSize, random) { randomBuf ->
            olm_create_sas(ptr, randomBuf, randomSize)
        }
        checkError(result)
    }

    actual fun clear() {
        olm_clear_sas(ptr)
        Native.free(Pointer.nativeValue(ptr.pointer))
    }

    /**
     * Gets the Public Key encoded in Base64 with no padding.
     *
     * @return The public key
     */
    actual val publicKey: String
        get() {
            val length = olm_sas_pubkey_length(ptr)
            return withAllocation(length.toLong()) {
                val result = olm_sas_get_pubkey(ptr, it, length)
                checkError(result)
                it.toKString(length.toInt())
            }
        }

    /**
     * Sets the public key of other user.
     *
     * @param theirPublicKey other user public key (base64 encoded with no padding)
     */
    actual fun setTheirPublicKey(theirPublicKey: String) {
        theirPublicKey.withNativeRead { theirPublicKeyPtr, theirPublicKeyLength ->
            val result = olm_sas_set_their_key(ptr, theirPublicKeyPtr, theirPublicKeyLength)
            checkError(result)
        }
    }

    /**
     * Generate bytes to use for the short authentication string.
     *
     * @param[info] info extra information to mix in when generating the bytes, as per the Matrix spec.
     * @param[numberOfBytes] The size of the short code to generate.
     * @return The generated short code.
     */
    actual fun generateShortCode(info: String, numberOfBytes: Int): ByteArray {
        val shortBytes = ByteArray(numberOfBytes)
        shortBytes.withNativeWrite { output ->
            info.withNativeRead { infoPtr, infoLen ->
                val result = olm_sas_generate_bytes(
                    ptr, infoPtr, infoLen,
                    output, NativeSize(numberOfBytes))
                checkError(result)
            }
        }
        return shortBytes
    }

    actual fun calculateMac(message: String, info: String): String {
        val macLength = olm_sas_mac_length(ptr)
        return withAllocation(macLength.toLong()) { macPtr ->
            message.withNativeRead { messagePtr, messageLen ->
                info.withNativeRead { infoPtr, infoLen ->
                    val result = olm_sas_calculate_mac(
                        ptr,
                        messagePtr, messageLen,
                        infoPtr, infoLen,
                        macPtr, macLength)
                    checkError(result)
                }
            }
            macPtr.toKString(macLength.toInt())
        }
    }

    actual fun calculateMacLongKdf(message: String, info: String): String {
        val macLength = olm_sas_mac_length(ptr)

        return withAllocation(macLength.toLong()) { macPtr ->
            message.withNativeRead { messagePtr, messageLen ->
                info.withNativeRead { infoPtr, infoLen ->
                    val result = olm_sas_calculate_mac_long_kdf(
                        ptr,
                        messagePtr, messageLen,
                        infoPtr, infoLen,
                        macPtr, macLength)
                    checkError(result)
                }
            }
            macPtr.toKString(macLength.toInt())
        }
    }

    private fun checkError(result: NativeSize) {
        genericCheckError(ptr, result, ::olm_sas_last_error)
    }
}
