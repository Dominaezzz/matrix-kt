package io.github.matrixkt.olm

import cnames.structs.OlmInboundGroupSession
import colm.internal.*
import kotlinx.cinterop.*
import platform.posix.size_t

/**
 * Class used to create an inbound [Megolm session](http://matrix.org/docs/guides/e2e_implementation.html#handling-an-m-room-key-event).
 * Counter part of the outbound group session [OutboundGroupSession], this class decrypts the messages sent by the outbound side.
 *
 * Detailed implementation guide is available at [Implementing End-to-End Encryption in Matrix clients](http://matrix.org/docs/guides/e2e_implementation.html).
 */
actual class InboundGroupSession private constructor(private val ptr: CPointer<OlmInboundGroupSession>) {
    /**
     * Create and save a new native session instance ID and start a new inbound group session.
     * The session key parameter is retrieved from an outbound group session.
     * @param sessionKey session key
     */
    actual constructor(sessionKey: String): this(genericInit(::olm_inbound_group_session, ::olm_inbound_group_session_size)) {
        try {
            val result = sessionKey.withNativeRead { sessionKeyPtr, sessionKeyLen ->
                olm_init_inbound_group_session(ptr, sessionKeyPtr?.reinterpret(), sessionKeyLen)
            }
            checkError(result)
        } catch (e: Exception) {
            clear()
            throw e
        }
    }

    actual fun clear() {
        olm_clear_inbound_group_session(ptr)
        nativeHeap.free(ptr)
    }

    /**
     * Retrieve the base64-encoded identifier for this inbound group session.
     * @return the session ID
     */
    actual val sessionId: String
        get() {
            val length = olm_inbound_group_session_id_length(ptr)
            val sessionId = ByteArray(length.convert())

            val result = sessionId.usePinned { sessionIdPtr ->
                olm_inbound_group_session_id(ptr, sessionIdPtr.addressOf(0).reinterpret(), length)
            }
            checkError(result)
            return sessionId.decodeToString()
        }

    /**
     * Provides the first known index.
     * @return the first known index.
     */
    actual val firstKnownIndex: Long get() = olm_inbound_group_session_first_known_index(ptr).convert()

    /**
     * Tells if the session is verified.
     * @return true if the session is verified
     */
    actual val isVerified: Boolean get() = olm_inbound_group_session_is_verified(ptr) != 0

    /**
     * Export the session from a message index as String.
     * @param messageIndex the message index
     * @return the session as String
     */
    actual fun export(messageIndex: Long): String {
        val length = olm_export_inbound_group_session_length(ptr)
        val buffer = ByteArray(length.convert())

        val result = buffer.usePinned {
            olm_export_inbound_group_session(ptr, it.addressOf(0).reinterpret(), length, messageIndex.convert())
        }
        checkError(result)
        return buffer.decodeToString()
    }

    /**
     * Decrypt the message passed in parameter.
     * In case of error, null is returned and an error message description is provided in aErrorMsg.
     * @param message the message to be decrypted
     * @return the decrypted message information
     */
    actual fun decrypt(message: String): GroupMessage {
        val maxPlainTextLen = message.withNativeRead { messagePtr, messageLen ->
            olm_group_decrypt_max_plaintext_length(ptr, messagePtr?.reinterpret(), messageLen)
        }
        checkError(maxPlainTextLen)

        return message.withNativeRead { messagePtr, messageLen ->
            val plainTextMessage = UByteArray(maxPlainTextLen.convert())
            memScoped {
                val messageIndex = alloc<UIntVar>()

                val length = olm_group_decrypt(ptr, messagePtr?.reinterpret(), messageLen,
                    plainTextMessage.refTo(0), maxPlainTextLen,
                    messageIndex.ptr)
                checkError(length)

                GroupMessage(
                    plainTextMessage.asByteArray().decodeToString(endIndex = length.convert()),
                    messageIndex.value.toLong()
                )
            }
        }
    }

    actual fun pickle(key: ByteArray): String {
        return genericPickle(ptr, key, ::olm_pickle_inbound_group_session_length, ::olm_pickle_inbound_group_session, ::checkError)
    }

    private fun checkError(result: size_t) {
        genericCheckError(ptr, result, ::olm_inbound_group_session_last_error)
    }

    actual companion object {
        private inline fun create(block: InboundGroupSession.() -> Unit): InboundGroupSession {
            val obj = InboundGroupSession(genericInit(::olm_inbound_group_session, ::olm_inbound_group_session_size))
            try {
                obj.block()
            } catch (e: Exception) {
                obj.clear() // Prevent leak
                throw e
            }
            return obj
        }

        actual fun import(sessionKey: String): InboundGroupSession {
            return create {
                val result = sessionKey.withNativeRead { sessionKeyPtr, sessionKeyLen ->
                    olm_import_inbound_group_session(ptr, sessionKeyPtr?.reinterpret(), sessionKeyLen)
                }
                checkError(result)
            }
        }

        actual fun unpickle(key: ByteArray, pickle: String): InboundGroupSession {
            return create {
                genericUnpickle(ptr, key, pickle, ::olm_unpickle_inbound_group_session, ::checkError)
            }
        }
    }
}
