package io.github.matrixkt.olm

/**
 * Message class used in Olm sessions to contain the encrypted data.
 *
 * See [Session.decrypt] and [Session.encrypt].
 *
 * Detailed implementation guide is available at [Implementing End-to-End Encryption in Matrix clients](http://matrix.org/docs/guides/e2e_implementation.html).
 */
public sealed class Message(
    /**
     * The encrypted message.
     */
    public val cipherText: String
) {
    /** PRE KEY message type (used to establish new Olm session)  */
    public class PreKey(cipherText: String) : Message(cipherText)

    /** Normal message type  */
    public class Normal(cipherText: String) : Message(cipherText)

    public val type: Long
        get() {
            return when (this) {
                is PreKey -> 0L
                is Normal -> 1L
            }
        }

    public companion object {
        public operator fun invoke(cipherText: String, type: Long): Message {
            return when (type) {
                0L -> PreKey(cipherText)
                1L -> Normal(cipherText)
                else -> throw NoSuchElementException("type = $type")
            }
        }
    }
}
