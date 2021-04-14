package io.github.matrixkt.olm

import kotlin.random.Random

public expect class OutboundGroupSession(random: Random = Random.Default) {
    public fun clear()

    /**
     * Retrieve the base64-encoded identifier for this outbound group session.
     * @return the session ID
     */
    public val sessionId: String

    /**
     * Get the current message index for this session.
     *
     * Each message is sent with an increasing index, this
     * method returns the index for the next message.
     * @return current session index
     */
    public val messageIndex: Int

    /**
     * Get the base64-encoded current ratchet key for this session.
     *
     * Each message is sent with a different ratchet key. This method returns the
     * ratchet key that will be used for the next message.
     * @return outbound session key
     */
    public val sessionKey: String

    /**
     * Encrypt some plain-text message.
     *
     * The message given as parameter is encrypted and returned as the return value.
     * @param plainText message to be encrypted
     * @return the encrypted message
     */
    public fun encrypt(plainText: String): String

    public fun pickle(key: ByteArray): String

    public companion object {
        public fun unpickle(key: ByteArray, pickle: String): OutboundGroupSession
    }
}
