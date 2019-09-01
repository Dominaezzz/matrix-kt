package io.github.matrixkt.olm

/**
 * Message class used in Olm sessions to contain the encrypted data.
 *
 * See [Session.decrypt] and [Session.encrypt].
 *
 * Detailed implementation guide is available at [Implementing End-to-End Encryption in Matrix clients](http://matrix.org/docs/guides/e2e_implementation.html).
 */
data class Message(
    /**
     * The encrypted message.
     */
    val cipherText: String,

    /**
     * Defined by [MESSAGE_TYPE_MESSAGE] or [MESSAGE_TYPE_PRE_KEY]
     */
    val type: Long
) {
    companion object {
        /** PRE KEY message type (used to establish new Olm session)  */
        const val MESSAGE_TYPE_PRE_KEY = 0
        /** Normal message type  */
        const val MESSAGE_TYPE_MESSAGE = 1
    }
}
