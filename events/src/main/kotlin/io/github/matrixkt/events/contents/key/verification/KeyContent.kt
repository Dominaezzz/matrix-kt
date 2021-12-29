package io.github.matrixkt.events.contents.key.verification

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Sends the ephemeral public key for a device to the partner device.
 */
public sealed class KeyContent {
    /**
     * The device's ephemeral public key, encoded as unpadded base64.
     */
    public abstract val key: String

    @SerialName("m.key.verification.key")
    @Serializable
    public data class ToDevice(
        /**
         * The opaque identifier for the verification process/request.
         * Must be the same as the one used for the `m.key.verification.start` message.
         */
        @SerialName("transaction_id")
        val transactionId: String,

        override val key: String
    ) : KeyContent()

    @SerialName("m.key.verification.key")
    @Serializable
    public data class InRoom(
        /**
         * Indicates the `m.key.verification.request` that this message is related to.
         * Note that for encrypted messages, this property should be in the unencrypted portion of the event.
         */
        @SerialName("m.relates_to")
        val relatesTo: VerificationRelatesTo,

        override val key: String
    ) : KeyContent()
}
