package io.github.matrixkt.olm

import colm.internal.NativeSize
import colm.internal.OlmLibrary.olm_clear_outbound_group_session
import colm.internal.OlmLibrary.olm_group_encrypt
import colm.internal.OlmLibrary.olm_group_encrypt_message_length
import colm.internal.OlmLibrary.olm_init_outbound_group_session
import colm.internal.OlmLibrary.olm_init_outbound_group_session_random_length
import colm.internal.OlmLibrary.olm_outbound_group_session
import colm.internal.OlmLibrary.olm_outbound_group_session_id
import colm.internal.OlmLibrary.olm_outbound_group_session_id_length
import colm.internal.OlmLibrary.olm_outbound_group_session_key
import colm.internal.OlmLibrary.olm_outbound_group_session_key_length
import colm.internal.OlmLibrary.olm_outbound_group_session_last_error
import colm.internal.OlmLibrary.olm_outbound_group_session_message_index
import colm.internal.OlmLibrary.olm_outbound_group_session_size
import colm.internal.OlmLibrary.olm_pickle_outbound_group_session
import colm.internal.OlmLibrary.olm_pickle_outbound_group_session_length
import colm.internal.OlmLibrary.olm_unpickle_outbound_group_session
import colm.internal.OlmOutboundGroupSession
import com.sun.jna.Native
import com.sun.jna.Pointer
import kotlin.random.Random

actual class OutboundGroupSession private constructor(private val ptr: OlmOutboundGroupSession) {
    actual constructor(random: Random): this(genericInit(::olm_outbound_group_session, ::olm_outbound_group_session_size)) {
        try {
            val randomLength = olm_init_outbound_group_session_random_length(ptr)
            val result = withRandomBuffer(randomLength, random) { randomBuff ->
                olm_init_outbound_group_session(ptr, randomBuff, randomLength)
            }
            checkError(result)
        } catch (e: Exception) {
            clear()
            throw e
        }
    }

    actual fun clear() {
        olm_clear_outbound_group_session(ptr)
        Native.free(Pointer.nativeValue(ptr.pointer))
    }

    /**
     * Retrieve the base64-encoded identifier for this outbound group session.
     * @return the session ID
     */
    actual val sessionId: String
        get() {
            val length = olm_outbound_group_session_id_length(ptr)
            return withAllocation(length.toLong()) { sessionId ->
                val result = olm_outbound_group_session_id(ptr, sessionId, length)
                checkError(result)
                sessionId.toKString(length.toInt())
            }
        }

    /**
     * Get the current message index for this session.
     *
     * Each message is sent with an increasing index, this
     * method returns the index for the next message.
     * @return current session index
     */
    actual val messageIndex: Int get() = olm_outbound_group_session_message_index(ptr)

    /**
     * Get the base64-encoded current ratchet key for this session.
     *
     * Each message is sent with a different ratchet key. This method returns the
     * ratchet key that will be used for the next message.
     * @return outbound session key
     */
    actual val sessionKey: String
        get() {
            val sessionKeyLength = olm_outbound_group_session_key_length(ptr)
            return withAllocation(sessionKeyLength.toLong()) { sessionKey ->
                val result = olm_outbound_group_session_key(ptr, sessionKey, sessionKeyLength)
                checkError(result)
                sessionKey.toKString(sessionKeyLength.toInt())
            }
        }

    /**
     * Encrypt some plain-text message.
     *
     * The message given as parameter is encrypted and returned as the return value.
     * @param plainText message to be encrypted
     * @return the encrypted message
     */
    actual fun encrypt(plainText: String): String {
        return plainText.withNativeRead { plainTextPtr, plainTextLength ->
            val encryptedMsgLength = olm_group_encrypt_message_length(ptr, plainTextLength)

            withAllocation(encryptedMsgLength.toLong()) { encryptedMsg ->
                val result = olm_group_encrypt(ptr, plainTextPtr, plainTextLength,
                    encryptedMsg, encryptedMsgLength)
                checkError(result)
                encryptedMsg.toKString(encryptedMsgLength.toInt())
            }
        }
    }

    actual fun pickle(key: ByteArray): String {
        return genericPickle(ptr, key, ::olm_pickle_outbound_group_session_length, ::olm_pickle_outbound_group_session, ::checkError)
    }

    private fun checkError(result: NativeSize) {
        genericCheckError(ptr, result, ::olm_outbound_group_session_last_error)
    }

    actual companion object {
        private inline fun create(block: OutboundGroupSession.() -> Unit): OutboundGroupSession {
            val obj = OutboundGroupSession(genericInit(::olm_outbound_group_session, ::olm_outbound_group_session_size))
            try {
                obj.block()
            } catch (e: Exception) {
                obj.clear() // Prevent leak
                throw e
            }
            return obj
        }

        actual fun unpickle(key: ByteArray, pickle: String): OutboundGroupSession {
            return create {
                genericUnpickle(ptr, key, pickle, ::olm_unpickle_outbound_group_session, ::checkError)
            }
        }
    }
}
