package io.github.matrixkt.olm

import kotlin.random.Random

public actual class SAS actual constructor(random: Random) {
    private val ptr = JsOlm.SAS()

    public actual fun clear() {
        ptr.free()
    }

    /**
     * Gets the Public Key encoded in Base64 with no padding.
     *
     * @return The public key
     */
    public actual val publicKey: String get() = ptr.get_pubkey()

    /**
     * Sets the public key of other user.
     *
     * @param theirPublicKey other user public key (base64 encoded with no padding)
     */
    public actual fun setTheirPublicKey(theirPublicKey: String) {
      ptr.set_their_key(theirPublicKey)
    }

    /**
     * Checks if their key was set.
     */
    public actual val isTheirKeySet: Boolean get() = ptr.is_their_key_set()

    /**
     * Generate bytes to use for the short authentication string.
     *
     * @param[info] info extra information to mix in when generating the bytes, as per the Matrix spec.
     * @param[numberOfBytes] The size of the short code to generate.
     * @return The generated short code.
     */
    public actual fun generateShortCode(info: String, numberOfBytes: Int): ByteArray {
        return ptr.generate_bytes(info, numberOfBytes).toString().encodeToByteArray()
    }

    public actual fun calculateMac(message: String, info: String): String {
        return ptr.calculate_mac(message, info)
    }

    public actual fun calculateMacLongKdf(message: String, info: String): String {
        return ptr.calculate_mac_long_kdf(message, info)
    }
}
