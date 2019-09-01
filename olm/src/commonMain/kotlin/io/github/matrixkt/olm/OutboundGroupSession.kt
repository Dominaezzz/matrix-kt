package io.github.matrixkt.olm

import kotlin.random.Random

expect class OutboundGroupSession(random: Random = Random.Default) {
    fun clear()

    /**
     * Retrieve the base64-encoded identifier for this outbound group session.
     * @return the session ID
     */
    val sessionId: String

    /**
     * Get the current message index for this session.
     *
     * Each message is sent with an increasing index, this
     * method returns the index for the next message.
     * @return current session index
     */
    val messageIndex: Int

    /**
     * Get the base64-encoded current ratchet key for this session.
     *
     * Each message is sent with a different ratchet key. This method returns the
     * ratchet key that will be used for the next message.
     * @return outbound session key
     */
    val sessionKey: String

    /**
     * Encrypt some plain-text message.
     *
     * The message given as parameter is encrypted and returned as the return value.
     * @param plainText message to be encrypted
     * @return the encrypted message
     */
    fun encrypt(plainText: String): String

    fun pickle(key: ByteArray): String

    companion object {
        fun unpickle(key: ByteArray, pickle: String): OutboundGroupSession
    }
}
