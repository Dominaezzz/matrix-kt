package io.github.matrixkt.olm

import colm.internal.*
import kotlinx.cinterop.*
import platform.posix.size_t
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
public actual class Session private constructor() {
    internal val ptr = genericInit(::olm_session, ::olm_session_size)

    public actual fun clear() {
        olm_clear_session(ptr)
        nativeHeap.free(ptr)
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
        get() {
            val sessionIdLength = olm_session_id_length(ptr)
            val id = ByteArray(sessionIdLength.convert())
            val result = olm_session_id(ptr, id.refTo(0), sessionIdLength)
            checkError(result)
            return id.decodeToString()
        }

    public actual val hasReceivedMessage: Boolean
        get() = olm_session_has_received_message(ptr) != 0

    public actual fun describe(): String {
        // Magic number pulled from here https://gitlab.matrix.org/matrix-org/olm/-/blob/b482321213e6e896d0981c266bed12f4e1f67441/javascript/olm_post.js#L465
        val bufferSize = 256
        val desc = ByteArray(bufferSize)
        olm_session_describe(ptr, desc.refTo(0), bufferSize.convert())
        return desc.decodeToString()
    }

    /**
     * Checks if the PRE_KEY([Message.MESSAGE_TYPE_PRE_KEY]) message is for this in-bound session.<br>
     * This API may be used to process a "m.room.encrypted" event when type = 1 (PRE_KEY).
     *
     * @param oneTimeKeyMsg PRE KEY message
     * @return true if the one time key matches.
     */
    public actual fun matchesInboundSession(oneTimeKeyMsg: String): Boolean {
        require(oneTimeKeyMsg.isNotBlank())

        val result = oneTimeKeyMsg.withNativeRead { oneTimeKeyMsgPtr, oneTimeKeyMsgSize ->
            olm_matches_inbound_session(ptr, oneTimeKeyMsgPtr, oneTimeKeyMsgSize)
        }
        return 1 == result.toInt()
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
        val result = theirIdentityKey.withNativeRead { theirIdentityKeyPtr, theirIdentityKeySize ->
            oneTimeKeyMsg.withNativeRead { oneTimeKeyMsgPtr, oneTimeKeyMsgSize ->
                olm_matches_inbound_session_from(
                    ptr,
                    theirIdentityKeyPtr, theirIdentityKeySize,
                    oneTimeKeyMsgPtr, oneTimeKeyMsgSize)
            }
        }
        return 1 == result.toInt()
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
        val messageType = olm_encrypt_message_type(ptr)

        return clearMsg.withNativeRead { clearMsgPtr, clearMsgSize ->
            val encryptedMsgLength = olm_encrypt_message_length(ptr, clearMsgSize)
            val encryptedMsg = ByteArray(encryptedMsgLength.convert())

            val randomBuffSize = olm_encrypt_random_length(ptr)
            val length = withRandomBuffer(randomBuffSize, random) { randomBuff ->
                olm_encrypt(
                    ptr, clearMsgPtr, clearMsgSize,
                    randomBuff, randomBuffSize,
                    encryptedMsg.refTo(0), encryptedMsgLength)
            }
            checkError(length)

            Message(encryptedMsg.decodeToString(endIndex = length.convert()), messageType.convert())
        }
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

        val maxPlainTextLength = encryptedMsg.cipherText.withNativeRead { encryptedMsgPtr, encryptedMsgSize ->
            olm_decrypt_max_plaintext_length(
                ptr, encryptedMsg.type.convert(), encryptedMsgPtr, encryptedMsgSize)
        }
        checkError(maxPlainTextLength)

        return encryptedMsg.cipherText.withNativeRead { encryptedMsgPtr, encryptedMsgSize ->
            val plainTextMsg = ByteArray(maxPlainTextLength.convert())
            val plainTextLength = olm_decrypt(
                ptr, encryptedMsg.type.convert(),
                encryptedMsgPtr, encryptedMsgSize,
                plainTextMsg.refTo(0), maxPlainTextLength)
            checkError(plainTextLength)

            plainTextMsg.decodeToString(endIndex = plainTextLength.convert())
        }
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
        return genericPickle(ptr, key, ::olm_pickle_session_length, ::olm_pickle_session, ::checkError)
    }

    private fun checkError(result: size_t) {
        genericCheckError(ptr, result, ::olm_session_last_error)
    }

    public actual companion object {
        private inline fun create(block: Session.() -> Unit): Session {
            val obj = Session()
            try {
                obj.block()
            } catch (e: Exception) {
                obj.clear() // Prevent leak
                throw e
            }
            return obj
        }

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
            return create {
                theirIdentityKey.withNativeRead { theirIdentityKeyPtr, theirIdentityKeySize ->
                    theirOneTimeKey.withNativeRead { theirOneTimeKeyPtr, theirOneTimeKeySize ->
                        val randomBuffSize = olm_create_outbound_session_random_length(ptr)
                        withRandomBuffer(randomBuffSize, random) { randomBuffPtr ->
                            val result = olm_create_outbound_session(
                                ptr, account.ptr,
                                theirIdentityKeyPtr, theirIdentityKeySize,
                                theirOneTimeKeyPtr, theirOneTimeKeySize,
                                randomBuffPtr, randomBuffSize)
                            checkError(result)
                        }
                    }
                }
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

            return create {
                val result = oneTimeKeyMsg.withNativeRead { oneTimeKeyMsgPtr, oneTimeKeyMsgSize ->
                    olm_create_inbound_session(ptr, account.ptr, oneTimeKeyMsgPtr, oneTimeKeyMsgSize)
                }
                checkError(result)
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

            return create {
                val result = theirIdentityKey.withNativeRead { theirIdentityKeyPtr, theirIdentityKeySize ->
                    oneTimeKeyMsg.withNativeRead { oneTimeKeyMsgPtr, oneTimeKeyMsgSize ->
                        olm_create_inbound_session_from(
                            ptr, account.ptr,
                            theirIdentityKeyPtr, theirIdentityKeySize,
                            oneTimeKeyMsgPtr, oneTimeKeyMsgSize)
                    }
                }
                checkError(result)
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
            return create {
                genericUnpickle(ptr, key, pickle, ::olm_unpickle_session, ::checkError)
            }
        }
    }
}
