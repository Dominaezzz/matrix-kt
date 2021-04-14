package io.github.matrixkt.olm

import colm.internal.*
import kotlinx.cinterop.*
import platform.posix.size_t
import kotlin.random.Random

public actual class SAS actual constructor(random: Random) {
    private val ptr = genericInit(::olm_sas, ::olm_sas_size)

    init {
        try {
            val randomSize = olm_create_sas_random_length(ptr)
            val result = withRandomBuffer(randomSize, random) { randomBuf ->
                olm_create_sas(ptr, randomBuf, randomSize)
            }
            checkError(result)
        } catch (e: Exception) {
            clear()
            throw e
        }
    }

    public actual fun clear() {
        olm_clear_sas(ptr)
        nativeHeap.free(ptr)
    }

    /**
     * Gets the Public Key encoded in Base64 with no padding.
     *
     * @return The public key
     */
    public actual val publicKey: String
        get() {
            val length = olm_sas_pubkey_length(ptr)
            val buffer = ByteArray(length.convert())
            val result = olm_sas_get_pubkey(ptr, buffer.refTo(0), length)
            checkError(result)
            return buffer.decodeToString()
        }

    /**
     * Sets the public key of other user.
     *
     * @param theirPublicKey other user public key (base64 encoded with no padding)
     */
    public actual fun setTheirPublicKey(theirPublicKey: String) {
        theirPublicKey.withNativeRead { theirPublicKeyPtr, theirPublicKeyLength ->
            val result = olm_sas_set_their_key(ptr, theirPublicKeyPtr, theirPublicKeyLength)
            checkError(result)
        }
    }

    /**
     * Checks if their key was set.
     */
    public actual val isTheirKeySet: Boolean get() = olm_sas_is_their_key_set(ptr) != 0

    /**
     * Generate bytes to use for the short authentication string.
     *
     * @param[info] info extra information to mix in when generating the bytes, as per the Matrix spec.
     * @param[numberOfBytes] The size of the short code to generate.
     * @return The generated short code.
     */
    public actual fun generateShortCode(info: String, numberOfBytes: Int): ByteArray {
        val shortBytes = ByteArray(numberOfBytes)
        info.withNativeRead { infoPtr, infoLen ->
            val result = olm_sas_generate_bytes(
                ptr, infoPtr, infoLen,
                shortBytes.refTo(0), numberOfBytes.convert())
            checkError(result)
        }
        return shortBytes
    }

    public actual fun calculateMac(message: String, info: String): String {
        val macLength = olm_sas_mac_length(ptr)
        val mac = ByteArray(macLength.convert())

        message.withNativeRead { messagePtr, messageLen ->
            info.withNativeRead { infoPtr, infoLen ->
                val result = olm_sas_calculate_mac(
                    ptr,
                    messagePtr, messageLen,
                    infoPtr, infoLen,
                    mac.refTo(0), macLength)
                checkError(result)
            }
        }
        return mac.decodeToString()
    }

    public actual fun calculateMacLongKdf(message: String, info: String): String {
        val macLength = olm_sas_mac_length(ptr)
        val mac = ByteArray(macLength.convert())

        message.withNativeRead { messagePtr, messageLen ->
            info.withNativeRead { infoPtr, infoLen ->
                val result = olm_sas_calculate_mac_long_kdf(
                    ptr,
                    messagePtr, messageLen,
                    infoPtr, infoLen,
                    mac.refTo(0), macLength)
                checkError(result)
            }
        }
        return mac.decodeToString()
    }

    private fun checkError(result: size_t) {
        genericCheckError(ptr, result, ::olm_sas_last_error)
    }
}
