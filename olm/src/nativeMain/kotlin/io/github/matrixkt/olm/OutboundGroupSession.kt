package io.github.matrixkt.olm

import colm.internal.*
import kotlinx.cinterop.*
import platform.posix.size_t
import kotlin.random.Random

actual class OutboundGroupSession private constructor(private val ptr: CPointer<OlmOutboundGroupSession>) {
    actual constructor(random: Random): this(genericInit(::olm_outbound_group_session, ::olm_outbound_group_session_size)) {
        try {
            val randomLength = olm_init_outbound_group_session_random_length(ptr)
            val result = withRandomBuffer(randomLength, random) { randomBuff ->
                olm_init_outbound_group_session(ptr, randomBuff?.reinterpret(), randomLength)
            }
            checkError(result)
        } catch (e: Exception) {
            clear()
            throw e
        }
    }

    actual fun clear() {
        olm_clear_outbound_group_session(ptr)
        nativeHeap.free(ptr)
    }

    /**
     * Retrieve the base64-encoded identifier for this outbound group session.
     * @return the session ID
     */
    actual val sessionId: String
        get() {
            val length = olm_outbound_group_session_id_length(ptr)
            val sessionId = ByteArray(length.convert())

            val result = sessionId.usePinned { sessionIdPtr ->
                olm_outbound_group_session_id(ptr, sessionIdPtr.addressOf(0).reinterpret(), length)
            }
            checkError(result)
            return sessionId.decodeToString()
        }

    /**
     * Get the current message index for this session.
     *
     * Each message is sent with an increasing index, this
     * method returns the index for the next message.
     * @return current session index
     */
    actual val messageIndex: Int get() = olm_outbound_group_session_message_index(ptr).convert()

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
            val sessionKey = ByteArray(sessionKeyLength.convert())

            sessionKey.usePinned {
                val result = olm_outbound_group_session_key(ptr, it.addressOf(0).reinterpret(), sessionKeyLength)
                checkError(result)
            }
            return sessionKey.decodeToString()
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
            val encryptedMsg = ByteArray(encryptedMsgLength.convert())

            val length = encryptedMsg.usePinned {
                olm_group_encrypt(ptr, plainTextPtr?.reinterpret(), plainTextLength.convert(),
                    it.addressOf(0).reinterpret(), encryptedMsgLength)
            }
            checkError(length)
            encryptedMsg.decodeToString(endIndex = length.convert())
        }
    }

    actual fun pickle(key: ByteArray): String {
        return genericPickle(ptr, key, ::olm_pickle_outbound_group_session_length, ::olm_pickle_outbound_group_session, ::checkError)
    }

    private fun checkError(result: size_t) {
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
