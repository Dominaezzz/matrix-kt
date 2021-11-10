package io.github.matrixkt.models.events.contents.key.verification

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Indicates that a verification process/request has completed successfully.
 */
public sealed class DoneContent {
    @SerialName("m.key.verification.done")
    @Serializable
    public data class ToDevice(
        /**
         * The opaque identifier for the verification process/request.
         */
        @SerialName("transaction_id")
        val transactionId: String,
    ) : DoneContent()

    @SerialName("m.key.verification.done")
    @Serializable
    public data class InRoom(
        /**
         * Indicates the `m.key.verification.request` that this message is related to.
         * Note that for encrypted messages, this property should be in the unencrypted portion of the event.
         */
        @SerialName("m.relates_to")
        val relatesTo: VerificationRelatesTo
    ) : DoneContent()
}
