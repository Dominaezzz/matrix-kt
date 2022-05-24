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
public actual class Session private constructor(internal val ptr: JsOlm.Session = JsOlm.Session()) {

    public actual fun clear() {
        ptr.free()
    }

    /**
     * Get the session identifier.
     *
     * Will be the same for both ends of the conversation.
     * The session identifier is returned as a String object.
     * Session Id sample: "session_id":"M4fOVwD6AABrkTKl"
     * Public API for {@link #getSessionIdentifierJni()}.
     * @return the session ID
     */
    public actual val sessionId: String
      get() = ptr.session_id()


    public actual val hasReceivedMessage: Boolean
        get() = ptr.has_received_message()

    public actual fun describe(): String = ptr.describe()

    /**
     * Checks if the PRE_KEY([Message.MESSAGE_TYPE_PRE_KEY]) message is for this in-bound session.<br>
     * This API may be used to process a "m.room.encrypted" event when type = 1 (PRE_KEY).
     *
     * @param oneTimeKeyMsg PRE KEY message
     * @return true if the one time key matches.
     */
    public actual fun matchesInboundSession(oneTimeKeyMsg: String): Boolean {
        require(oneTimeKeyMsg.isNotBlank())
        return ptr.matches_inbound(oneTimeKeyMsg)
    }

    /**
     * Checks if the PRE_KEY([Message.MESSAGE_TYPE_PRE_KEY]) message is for this in-bound session based on the sender identity key.<br>
     * This API may be used to process a "m.room.encrypted" event when type = 1 (PRE_KEY).
     *
     * @param theirIdentityKey the sender identity key
     * @param oneTimeKeyMsg PRE KEY message
     * @return this if operation succeed, null otherwise
     */
    public actual fun matchesInboundSessionFrom(theirIdentityKey: String, oneTimeKeyMsg: String): Boolean {
        return ptr.matches_inbound_from(theirIdentityKey, oneTimeKeyMsg)
    }

    /**
     * Encrypt a message using the session.
     *
     * The encrypted message is returned in a Message object.
     *
     * @param clearMsg message to encrypted
     * @return the encrypted message
     */
    public actual fun encrypt(clearMsg: String, random: Random): Message {
        return ptr.encrypt(clearMsg)
    }

    /**
     * Decrypt a message using the session.
     * The encrypted message is given as a [Message] object.
     * @param encryptedMsg message to decrypt
     * @return the decrypted message
     */
    public actual fun decrypt(encryptedMsg: Message): String {
        // olm_decrypt_max_plaintext_length and olm_decrypt destroy the input buffer
        // Hence the need for two `withNativeRead`s. Should optimize this later.
        return ptr.decrypt(encryptedMsg.type.toInt(), encryptedMsg.cipherText)
    }

    /**
     * Return a session as a bytes buffer.
     *
     * The account is serialized and encrypted with [key].
     *
     * @param[key] encryption key
     * @return the session as bytes buffer
     */
    public actual fun pickle(key: ByteArray): String {
        return ptr.pickle(key)
    }

    public actual companion object {

        /**
         * Creates a new out-bound session for sending messages to a recipient
         * identified by an identity key and a one time key.
         *
         * @param account the account to associate with this session
         * @param theirIdentityKey the identity key of the recipient
         * @param theirOneTimeKey the one time key of the recipient
         */
        public actual fun createOutboundSession(account: Account, theirIdentityKey: String, theirOneTimeKey: String, random: Random): Session {
            require(theirIdentityKey.isNotBlank())
            require(theirOneTimeKey.isNotBlank())
            val jsSession = JsOlm.Session()
            try {
                jsSession.create_outbound(account.ptr, theirIdentityKey, theirOneTimeKey)
                return Session(jsSession)
            } catch (e: Exception) {
                jsSession.free()
                throw e
            }
        }

        /**
         * Create a new in-bound session for sending/receiving messages from an
         * incoming PRE_KEY message ([Message.MESSAGE_TYPE_PRE_KEY]).
         *
         * This API may be used to process a "m.room.encrypted" event when type = 1 (PRE_KEY).
         * @param [account] the account to associate with this session
         * @param [oneTimeKeyMsg] PRE KEY message
         */
        public actual fun createInboundSession(account: Account, oneTimeKeyMsg: String): Session {
            require(oneTimeKeyMsg.isNotBlank())
            val jsSession = JsOlm.Session()
            try {
                jsSession.create_inbound(account.ptr, oneTimeKeyMsg)
                return Session(jsSession)
            } catch (e: Exception) {
                jsSession.free()
                throw e
            }
        }

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
        public actual fun createInboundSessionFrom(account: Account, theirIdentityKey: String, oneTimeKeyMsg: String): Session {
            require(theirIdentityKey.isNotBlank())
            require(oneTimeKeyMsg.isNotBlank())
            val jsSession = JsOlm.Session()
            try {
                jsSession.create_inbound_from(account.ptr, theirIdentityKey, oneTimeKeyMsg)
                return Session(jsSession)
            } catch (e: Exception) {
                jsSession.free()
                throw e
            }
        }

        /**
         * Loads a session from a pickled bytes buffer.
         *
         * @see[pickle]
         * @param[pickle] bytes buffer
         * @param[key] key used to encrypt
         * @exception Exception the exception
         */
        public actual fun unpickle(key: ByteArray, pickle: String): Session {
            val session = JsOlm.Session()
            try {
                session.unpickle(key, pickle)
                return Session(session)
            } catch (e: Exception) {
                session.free()
                throw e
            }
        }
    }
}
