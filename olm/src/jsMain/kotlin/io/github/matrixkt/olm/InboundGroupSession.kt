package io.github.matrixkt.olm

public actual class InboundGroupSession private constructor(private val ptr: JsOlm.InboundGroupSession) {

    public actual constructor(sessionKey: String): this(JsOlm.InboundGroupSession()) {
        try {
            ptr.create(sessionKey)
        } catch (e: Exception) {
            clear()
            throw e
        }
    }

    public actual fun clear () {
        ptr.free();
    }

    /**
     * Retrieve the base64-encoded identifier for this inbound group session.
     * @return the session ID
     */
    public actual val sessionId: String get() = ptr.session_id()

    /**
     * Provides the first known index.
     * @return the first known index.
     */
    public actual val firstKnownIndex: Long get() = ptr.first_known_index()

    /**
     * Tells if the session is verified.
     * @return true if the session is verified
     */
    public actual val isVerified: Boolean
        get()  {
            throw NotImplementedError("isVerified is not implemented in matrix-org/olm js implementation")
        }

    /**
     * Export the session from a message index as String.
     * @param messageIndex the message index
     * @return the session as String
     */
    public actual fun export(messageIndex: Long): String {
        return ptr.export_session(messageIndex)
    }

    /**
     * Decrypt the message passed in parameter.
     * In case of error, null is returned and an error message description is provided in aErrorMsg.
     * @param message the message to be decrypted
     * @return the decrypted message information
     */
    public actual fun decrypt(message: String): GroupMessage {
      return ptr.decrypt(message)
    }

    public actual fun pickle(key: ByteArray): String {
        return ptr.pickle(key.toString())
    }

    public actual companion object {
        public actual fun import(sessionKey: String): InboundGroupSession {
            val session = JsOlm.InboundGroupSession()
            try {
                session.import_session(sessionKey)
            } catch (e: Exception) {
                session.free()
                throw e
            }
            return InboundGroupSession(session)
        }

        /**
         * Loads an Inbound group session from a pickled bytes buffer.
         *
         * @see [pickle]
         * @param[key] key used to encrypt
         * @param[pickle] bytes buffer
         */
        public actual fun unpickle(key: ByteArray, pickle: String): InboundGroupSession {
            val session = JsOlm.InboundGroupSession()
            try {
                session.unpickle(key.toString(), pickle)
            } catch (e: Exception) {
                session.free()
                throw e
            }
            return InboundGroupSession(session)
        }
    }
}