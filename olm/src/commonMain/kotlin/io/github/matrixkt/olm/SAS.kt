package io.github.matrixkt.olm

import kotlin.random.Random

expect class SAS(random: Random = Random.Default) {
    fun clear()

    /**
     * Gets the Public Key encoded in Base64 with no padding.
     *
     * @return The public key
     */
    val publicKey: String

    /**
     * Sets the public key of other user.
     *
     * @param theirPublicKey other user public key (base64 encoded with no padding)
     */
    fun setTheirPublicKey(theirPublicKey: String)

    /**
     * Checks if their key was set.
     */
    val isTheirKeySet: Boolean

    /**
     * Generate bytes to use for the short authentication string.
     *
     * @param[info] info extra information to mix in when generating the bytes, as per the Matrix spec.
     * @param[numberOfBytes] The size of the short code to generate.
     * @return The generated short code.
     */
    fun generateShortCode(info: String, numberOfBytes: Int): ByteArray

    fun calculateMac(message: String, info: String): String

    fun calculateMacLongKdf(message: String, info: String): String
}
