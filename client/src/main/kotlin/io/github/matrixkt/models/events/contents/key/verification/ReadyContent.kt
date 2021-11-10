package io.github.matrixkt.models.events.contents.key.verification

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Accepts a key verification request.
 * Sent in response to an `m.key.verification.request` event.
 */
public sealed class ReadyContent {
    /**
     * The device ID which is accepting the request.
     */
    @SerialName("from_device")
    public abstract val fromDevice: String

    /**
     * The verification methods supported by the sender, corresponding to the
     * verification methods indicated in the `m.key.verification.request` message.
     */
    public abstract val methods: List<String>

    @SerialName("m.key.verification.ready")
    @Serializable
    public data class ToDevice(
        @SerialName("from_device")
        override val fromDevice: String,

        override val methods: List<String>,

        /**
         * The transaction ID of the verification request, as given in the `m.key.verification.request` message.
         */
        @SerialName("transaction_id")
        val transactionId: String,
    ) : ReadyContent()

    @SerialName("m.key.verification.ready")
    @Serializable
    public data class InRoom(
        @SerialName("from_device")
        override val fromDevice: String,

        override val methods: List<String>,

        /**
         * Indicates the `m.key.verification.request` that this message is related to.
         * Note that for encrypted messages, this property should be in the unencrypted portion of the event.
         */
        @SerialName("m.relates_to")
        val relatesTo: VerificationRelatesTo
    ) : ReadyContent()
}
