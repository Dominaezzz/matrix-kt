package io.github.matrixkt.olm

import kotlin.random.Random


/**
 * Session class used to create Olm sessions in conjunction with [Account] class.
 *
 * Olm session is used to encrypt data between devices, especially to create Olm group sessions [OutboundGroupSession] and [InboundGroupSession].
 *
 * To establish an Olm session with Bob, Alice calls [createOutboundSession] with Bob's identity and onetime keys.
 * Then Alice generates an encrypted PRE_KEY message [encrypt]
 * used by Bob to open the Olm session in his side with [createOutboundSession].
 * From this step on, messages can be exchanged by using [encrypt] and [decrypt].
 *
 * Detailed implementation guide is available at [Implementing End-to-End Encryption in Matrix clients](http://matrix.org/docs/guides/e2e_implementation.html).
 */
expect class Session {
    fun clear()

    /**
     * Get the session identifier.
     *
     * Will be the same for both ends of the conversation.
     * The session identifier is returned as a String object.
     * Session Id sample: "session_id":"M4fOVwD6AABrkTKl"
     *
     * @return the session ID
     */
    val sessionId: String

    /**
     * Checks if the PRE_KEY([Message.MESSAGE_TYPE_PRE_KEY]) message is for this in-bound session.
     *
     * This API may be used to process a "m.room.encrypted" event when type = 1 (PRE_KEY).
     *
     * @param oneTimeKeyMsg PRE KEY message
     * @return true if the one time key matches.
     */
    fun matchesInboundSession(oneTimeKeyMsg: String): Boolean

    /**
     * Checks if the PRE_KEY([Message.MESSAGE_TYPE_PRE_KEY]) message is for this in-bound session based on the sender identity key.
     *
     * This API may be used to process a "m.room.encrypted" event when type = 1 (PRE_KEY).
     *
     * @param theirIdentityKey the sender identity key
     * @param oneTimeKeyMsg PRE KEY message
     * @return this if operation succeed, null otherwise
     */
    fun matchesInboundSessionFrom(theirIdentityKey: String, oneTimeKeyMsg: String): Boolean

    /**
     * Encrypt a message using the session.
     *
     * The encrypted message is returned in a Message object.
     *
     * @param clearMsg message to encrypted
     * @return the encrypted message
     */
    fun encrypt(clearMsg: String, random: Random = Random.Default): Message

    /**
     * Decrypt a message using the session.
     *
     * The encrypted message is given as a [Message] object.
     * @param encryptedMsg message to decrypt
     * @return the decrypted message
     */
    fun decrypt(encryptedMsg: Message): String

    /**
     * Return a session as a base64 pickle.
     *
     * The account is serialized and encrypted with [key].
     *
     * @param[key] encryption key
     * @return the session as base64 pickle
     */
    fun pickle(key: ByteArray): String

    companion object {
        /**
         * Creates a new out-bound session for sending messages to a recipient
         * identified by an identity key and a one time key.
         *
         * @param account the account to associate with this session
         * @param theirIdentityKey the identity key of the recipient
         * @param theirOneTimeKey the one time key of the recipient
         */
        fun createOutboundSession(account: Account, theirIdentityKey: String, theirOneTimeKey: String, random: Random = Random.Default): Session

        /**
         * Create a new in-bound session for sending/receiving messages from an
         * incoming PRE_KEY message ([Message.MESSAGE_TYPE_PRE_KEY]).
         *
         * This API may be used to process a "m.room.encrypted" event when type = 1 (PRE_KEY).
         * @param [account] the account to associate with this session
         * @param [oneTimeKeyMsg] PRE KEY message
         */
        fun createInboundSession(account: Account, oneTimeKeyMsg: String): Session

        /**
         * Create a new in-bound session for sending/receiving messages from an
         * incoming PRE_KEY([Message.MESSAGE_TYPE_PRE_KEY]) message based on the sender identity key.
         *
         * This API may be used to process a "m.room.encrypted" event when type = 1 (PRE_KEY).
         * This method must only be called the first time a pre-key message is received from an inbound session.
         * @param account the account to associate with this session
         * @param theirIdentityKey the sender identity key
         * @param oneTimeKeyMsg PRE KEY message
         */
        fun createInboundSessionFrom(account: Account, theirIdentityKey: String, oneTimeKeyMsg: String): Session

        /**
         * Loads a session from a pickled bytes buffer.
         *
         * @see[pickle]
         * @param[key] key used to encrypt
         * @param[pickle] base64 string
         */
        fun unpickle(key: ByteArray, pickle: String): Session
    }
}
