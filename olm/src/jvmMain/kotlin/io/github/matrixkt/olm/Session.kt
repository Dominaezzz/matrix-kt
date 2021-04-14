package io.github.matrixkt.olm

import colm.internal.NativeSize
import colm.internal.OlmLibrary.olm_clear_session
import colm.internal.OlmLibrary.olm_create_inbound_session
import colm.internal.OlmLibrary.olm_create_inbound_session_from
import colm.internal.OlmLibrary.olm_create_outbound_session
import colm.internal.OlmLibrary.olm_create_outbound_session_random_length
import colm.internal.OlmLibrary.olm_decrypt
import colm.internal.OlmLibrary.olm_decrypt_max_plaintext_length
import colm.internal.OlmLibrary.olm_encrypt
import colm.internal.OlmLibrary.olm_encrypt_message_length
import colm.internal.OlmLibrary.olm_encrypt_message_type
import colm.internal.OlmLibrary.olm_encrypt_random_length
import colm.internal.OlmLibrary.olm_matches_inbound_session
import colm.internal.OlmLibrary.olm_matches_inbound_session_from
import colm.internal.OlmLibrary.olm_pickle_session
import colm.internal.OlmLibrary.olm_pickle_session_length
import colm.internal.OlmLibrary.olm_session
import colm.internal.OlmLibrary.olm_session_describe
import colm.internal.OlmLibrary.olm_session_has_received_message
import colm.internal.OlmLibrary.olm_session_id
import colm.internal.OlmLibrary.olm_session_id_length
import colm.internal.OlmLibrary.olm_session_last_error
import colm.internal.OlmLibrary.olm_session_size
import colm.internal.OlmLibrary.olm_unpickle_session
import com.sun.jna.Native
import com.sun.jna.Pointer
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
        Native.free(Pointer.nativeValue(ptr.pointer))
    }

    /**
     * Get the session identifier.
     *
     * Will be the same for both ends of the conversation.
     * The session identifier is returned as a String object.
     * Session Id sample: "session_id":"M4fOVwD6AABrkTKl"
     * @return the session ID
     */
    public actual val sessionId: String
        get() {
            val sessionIdLength = olm_session_id_length(ptr)
            return withAllocation(sessionIdLength.toLong()) {
                val result = olm_session_id(ptr, it, sessionIdLength)
                checkError(result)
                it.toKString(sessionIdLength.toInt())
            }
        }

    public actual val hasReceivedMessage: Boolean
        get() = olm_session_has_received_message(ptr) != 0

    public actual fun describe(): String {
        // Magic number pulled from here https://gitlab.matrix.org/matrix-org/olm/-/blob/b482321213e6e896d0981c266bed12f4e1f67441/javascript/olm_post.js#L465
        val bufferSize = 256
        return withAllocation(bufferSize.toLong()) { desc ->
            olm_session_describe(ptr, desc, NativeSize(bufferSize))
            desc.toKString(bufferSize)
        }
    }

    /**
     * Checks if the PRE_KEY([Message.MESSAGE_TYPE_PRE_KEY]) message is for this in-bound session.
     *
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
     * Checks if the PRE_KEY([Message.MESSAGE_TYPE_PRE_KEY]) message is for this in-bound session based on the sender identity key.
     *
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
                    ptr, theirIdentityKeyPtr, theirIdentityKeySize,
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
            withAllocation(encryptedMsgLength.toLong()) { encryptedMsgPtr ->
                val randomBuffSize = olm_encrypt_random_length(ptr)
                val result = withRandomBuffer(randomBuffSize, random) { randomBuff ->
                    olm_encrypt(
                        ptr, clearMsgPtr, clearMsgSize,
                        randomBuff, randomBuffSize,
                        encryptedMsgPtr, encryptedMsgLength)
                }
                checkError(result)
                Message(encryptedMsgPtr.toKString(encryptedMsgLength.toInt()), messageType.toLong())
            }
        }
    }

    /**
     * Decrypt a message using the session.
     *
     * The encrypted message is given as a [Message] object.
     * @param encryptedMsg message to decrypt
     * @return the decrypted message
     */
    public actual fun decrypt(encryptedMsg: Message): String {
        // olm_decrypt_max_plaintext_length and olm_decrypt destroy the input buffer
        // Hence the need for two `withNativeRead`s. Should optimize this later.

        val maxPlainTextLength = encryptedMsg.cipherText.withNativeRead { encryptedMsgPtr, encryptedMsgSize ->
            olm_decrypt_max_plaintext_length(ptr, NativeSize(encryptedMsg.type), encryptedMsgPtr, encryptedMsgSize)
        }
        checkError(maxPlainTextLength)

        return encryptedMsg.cipherText.withNativeRead { encryptedMsgPtr, encryptedMsgSize ->
            withAllocation(maxPlainTextLength.toLong()) {
                val plainTextLength = olm_decrypt(ptr, NativeSize(encryptedMsg.type),
                    encryptedMsgPtr, encryptedMsgSize, it, maxPlainTextLength)
                checkError(plainTextLength)
                it.toKString(plainTextLength.toInt())
            }
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

    private fun checkError(result: NativeSize) {
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
                val result = theirIdentityKey.withNativeRead { theirIdentityKeyPtr, theirIdentityKeySize ->
                    theirOneTimeKey.withNativeRead { theirOneTimeKeyPtr, theirOneTimeKeySize ->
                        val randomBuffSize = olm_create_outbound_session_random_length(ptr)
                        withRandomBuffer(randomBuffSize, random) { randomBuffPtr ->
                            olm_create_outbound_session(
                                ptr, account.ptr,
                                theirIdentityKeyPtr, theirIdentityKeySize,
                                theirOneTimeKeyPtr, theirOneTimeKeySize,
                                randomBuffPtr, randomBuffSize)
                        }
                    }
                }
                checkError(result)
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
                val result = oneTimeKeyMsg.withNativeRead { oneTimeKeyPtr, oneTimeKeySize ->
                    olm_create_inbound_session(ptr, account.ptr, oneTimeKeyPtr, oneTimeKeySize)
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
                    oneTimeKeyMsg.withNativeRead { theirOneTimeKeyPtr, theirOneTimeKeySize ->
                        olm_create_inbound_session_from(
                            ptr, account.ptr,
                            theirIdentityKeyPtr, theirIdentityKeySize,
                            theirOneTimeKeyPtr, theirOneTimeKeySize)
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
         */
        public actual fun unpickle(key: ByteArray, pickle: String): Session {
            return create {
                genericUnpickle(ptr, key, pickle, ::olm_unpickle_session, ::checkError)
            }
        }
    }
}
