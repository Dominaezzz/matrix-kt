package io.github.matrixkt.models.events.contents.key.verification

import io.github.matrixkt.models.events.contents.Content
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class KeyContent(
    /**
     * An opaque identifier for the verification process.
     * Must be the same as the one used for the `m.key.verification.start` message.
     */
    @SerialName("transaction_id")
    val transactionId: String,

    /**
     * The device's ephemeral public key, encoded as unpadded base64.
     */
    val key: String
) : Content()
