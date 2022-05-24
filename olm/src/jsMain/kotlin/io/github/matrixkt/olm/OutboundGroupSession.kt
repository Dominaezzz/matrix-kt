package io.github.matrixkt.olm

import kotlin.random.Random

public actual class OutboundGroupSession private constructor(private val ptr: JsOlm.OutboundGroupSession) {
    public actual constructor(random: Random): this(JsOlm.OutboundGroupSession()) {
        try {
            ptr.create()
        } catch (e: Exception) {
            clear()
            throw e
        }
    }

    public actual fun clear() {
        ptr.free()
    }

    /**
     * Retrieve the base64-encoded identifier for this outbound group session.
     * @return the session ID
     */
    public actual val sessionId: String
        get() {
            return ptr.session_id()
        }

    /**
     * Get the current message index for this session.
     *
     * Each message is sent with an increasing index, this
     * method returns the index for the next message.
     * @return current session index
     */
    public actual val messageIndex: Int get() = ptr.message_index()

    /**
     * Get the base64-encoded current ratchet key for this session.
     *
     * Each message is sent with a different ratchet key. This method returns the
     * ratchet key that will be used for the next message.
     * @return outbound session key
     */
    public actual val sessionKey: String
        get() {
            return ptr.session_key()
        }

    /**
     * Encrypt some plain-text message.
     *
     * The message given as parameter is encrypted and returned as the return value.
     * @param plainText message to be encrypted
     * @return the encrypted message
     */
    public actual fun encrypt(plainText: String): String {
       return ptr.encrypt(plainText)
    }

    public actual fun pickle(key: ByteArray): String {
        return ptr.pickle(key.toString())
    }

    public actual companion object {
        /**
         * Loads an account from a pickled bytes buffer.
         *
         * @see [pickle]
         * @param[key] key used to encrypt
         * @param[pickle] bytes buffer
         */
        public actual fun unpickle(key: ByteArray, pickle: String): OutboundGroupSession {
            val session = JsOlm.OutboundGroupSession()
            try {
                session.unpickle(key.toString(), pickle)
            } catch (e: Exception) {
                session.free()
                throw e
            }
            return OutboundGroupSession(session)
        }
    }
}
