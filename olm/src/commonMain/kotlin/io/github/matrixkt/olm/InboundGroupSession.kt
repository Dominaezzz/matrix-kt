package io.github.matrixkt.olm


/**
 * Class used to create an inbound [Megolm session](http://matrix.org/docs/guides/e2e_implementation.html#handling-an-m-room-key-event).
 * Counter part of the outbound group session [OutboundGroupSession], this class decrypts the messages sent by the outbound side.
 *
 * Detailed implementation guide is available at [Implementing End-to-End Encryption in Matrix clients](http://matrix.org/docs/guides/e2e_implementation.html).
 */
public expect class InboundGroupSession
/**
 * Create and save a new native session instance ID and start a new inbound group session.
 * The session key parameter is retrieved from an outbound group session.
 * @param sessionKey session key
 */
constructor(sessionKey: String)
{
    public fun clear()

    /**
     * Retrieve the base64-encoded identifier for this inbound group session.
     * @return the session ID
     */
    public val sessionId: String

    /**
     * Provides the first known index.
     * @return the first known index.
     */
    public val firstKnownIndex: Long

    /**
     * Tells if the session is verified.
     * @return true if the session is verified
     */
    public val isVerified: Boolean

    /**
     * Export the session from a message index as String.
     * @param messageIndex the message index
     * @return the session as String
     */
    public fun export(messageIndex: Long): String

    /**
     * Decrypt the message passed in parameter.
     * In case of error, null is returned and an error message description is provided in aErrorMsg.
     * @param message the message to be decrypted
     * @return the decrypted message information
     */
    public fun decrypt(message: String): GroupMessage

    public fun pickle(key: ByteArray): String

    public companion object {
        public fun import(sessionKey: String): InboundGroupSession

        public fun unpickle(key: ByteArray, pickle: String): InboundGroupSession
    }
}
