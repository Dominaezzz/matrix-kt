package io.github.matrixkt.olm

import kotlin.random.Random

public expect class SAS(random: Random = Random.Default) {
    public fun clear()

    /**
     * Gets the Public Key encoded in Base64 with no padding.
     *
     * @return The public key
     */
    public val publicKey: String

    /**
     * Sets the public key of other user.
     *
     * @param theirPublicKey other user public key (base64 encoded with no padding)
     */
    public fun setTheirPublicKey(theirPublicKey: String)

    /**
     * Checks if their key was set.
     */
    public val isTheirKeySet: Boolean

    /**
     * Generate bytes to use for the short authentication string.
     *
     * @param[info] info extra information to mix in when generating the bytes, as per the Matrix spec.
     * @param[numberOfBytes] The size of the short code to generate.
     * @return The generated short code.
     */
    public fun generateShortCode(info: String, numberOfBytes: Int): ByteArray

    public fun calculateMac(message: String, info: String): String

    public fun calculateMacLongKdf(message: String, info: String): String
}
